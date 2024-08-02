package com.varun.streaming.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.varun.streaming.domain.Quality;
import com.varun.streaming.domain.Video;
import com.varun.streaming.service.ProcessingService;
import com.varun.streaming.utility.FileUtility;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProcessingServiceImpl implements ProcessingService {

	@Value("${minio.bucket.videos}")
	private String videosBucket;

	@Autowired
	private MinioClient minioClient;

	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	public static final String MAIN_PATH = "/home/developer/sdata";

	@Override
	public void processVideoToHLS(Video video) throws Exception {
		File mainDirFile = null;
		try {
			mainDirFile = storeVideoToTemp(video.getId(), video.getName(), videosBucket);
			String inputFilePath = mainDirFile.getAbsolutePath() + FILE_SEPARATOR + video.getName();

			for (Quality quality : video.getQualities()) {
				String outputDir = String.format("%s%s%s", mainDirFile, FILE_SEPARATOR, quality.getType());

				File outputDirFile = new File(outputDir);
				outputDirFile.mkdirs();
				// Generate HLS files using FFmpeg
				String command = String.format(
						"ffmpeg -i %s -s %s -c:v libx264 -crf 23 -preset veryfast -c:a aac -b:a 128k "
								+ "-hls_time 10 -hls_list_size 0 -f hls %s/sample.m3u8",
						inputFilePath, quality.getResolution(), outputDir);
				Process process = Runtime.getRuntime().exec(command);

				// Wait for process to complete
				int exitCode = process.waitFor();
				if (exitCode != 0) {
					throw new RuntimeException("FFmpeg process failed with exit code: " + exitCode);
				}

				// Upload HLS files to MinIO
				uploadDirectoryToMinIO(outputDir, video.getId() + "/" + quality.getType());
			}
		} catch (Exception e) {
			log.error("Error processing video: {}", e.getMessage(),e);
		} finally {
			// Cleanup: delete temporary directory and files
			if (mainDirFile != null) {
				FileUtility.deleteDirectory(mainDirFile);
			}
			log.info("Video processing completed.");
		}
	}

	private File storeVideoToTemp(Long id, String name, String bucket) {
		// Create main directory for video processing
		File mainDirFile = new File(MAIN_PATH + FILE_SEPARATOR + bucket + FILE_SEPARATOR + id);
		mainDirFile.mkdirs();
		String inputFilePath = mainDirFile.getAbsolutePath() + FILE_SEPARATOR + name;
		log.info("File path: {}", inputFilePath);

		try (InputStream inputStream = minioClient
				.getObject(GetObjectArgs.builder().bucket(bucket).object(id + "/original.mp4").build());
				FileOutputStream fileOutputStream = new FileOutputStream(inputFilePath)) {
			byte[] buf = new byte[8192];
			int bytesRead;
			while ((bytesRead = inputStream.read(buf)) != -1) {
				fileOutputStream.write(buf, 0, bytesRead);
			}
			log.info("File downloaded from MinIO to path: {}", inputFilePath);
		} catch (MinioException e) {
			throw new RuntimeException("Error occurred while accessing MinIO", e);
		} catch (Exception e) {
			throw new RuntimeException("Error occurred while accessing MinIO", e);
		}

		return mainDirFile;
	}

	private void uploadDirectoryToMinIO(String dirPath, String minioPath) throws Exception {
		File dir = new File(dirPath);
		for (File file : dir.listFiles()) {
			minioClient.putObject(PutObjectArgs.builder().bucket(videosBucket).object(minioPath + "/" + file.getName())
					.stream(new FileInputStream(file), file.length(), -1).contentType("application/vnd.apple.mpegurl")
					.build());
		}
	}

}
