package com.varun.streaming.service.impl;

import java.util.List;
import java.util.Random;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.varun.streaming.dao.VideoDao;
import com.varun.streaming.domain.Quality;
import com.varun.streaming.domain.Video;
import com.varun.streaming.service.VideoService;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;

@Service
public class VideoServiceImpl implements VideoService {

	@Value("${minio.bucket.videos}")
	private String videosBucket;

	@Value("${minio.bucket.thumbnails}")
	private String thumbnailsBucket;

	@Autowired
	private MinioClient minioClient;

	@Autowired
	private VideoDao videoDao;

	@Autowired
	private Gson gson;

	@Autowired
	@Qualifier("videosRabbitTemplate")
	private RabbitTemplate rabbitTemplate;

	private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	private Random random = new Random();

	@Override
	@Transactional(value = "videosTransactionManager", rollbackFor = Exception.class)
	public void uploadVideo(MultipartFile file, MultipartFile thumbnail, Double duration, Integer resolution)
			throws Exception {
		String fileName = file.getOriginalFilename().replace(".mp4", "");
		Video video = new Video();
		video.setName(fileName);
		video.setUserId(1234l);
		video.setDescription(fileName);
		video.setDuration(duration);
		video.setThumbnail(generateUniqueThumbnailPath(video.getName(), video.getUserId()));
		video.setQualities(Quality.getQualitiesForResolution(resolution));

		video = videoDao.insertVideo(video);

		String originalPath = video.getId() + "/original.mp4";
		minioClient.putObject(PutObjectArgs.builder().bucket(videosBucket).object(originalPath)
				.stream(file.getInputStream(), file.getSize(), -1).contentType(file.getContentType()).build());

		minioClient.putObject(PutObjectArgs.builder().bucket(thumbnailsBucket).object(video.getThumbnail())
				.stream(thumbnail.getInputStream(), thumbnail.getSize(), -1).contentType(thumbnail.getContentType())
				.build());
		pushVideosToQueue(video);
	}

	private void pushVideosToQueue(Video video) {
		rabbitTemplate.convertAndSend("videos", gson.toJson(video));
	}

	private String generateUniqueThumbnailPath(String fileName, Long userId) throws RuntimeException {
		String thumbnailPath;
		for (int i = 0; i < 10; i++) {
			thumbnailPath = fileName.substring(0, Math.min(4, fileName.length())) + "_" + userId + "_"
					+ generateRandomAlphanumeric(4) + ".jpg";

			if (!isThumbnailExists(thumbnailPath)) {
				return thumbnailPath;
			}
		}
		throw new RuntimeException("Failed to generate a unique thumbnail path after 10 attempts.");
	}

	private String generateRandomAlphanumeric(int length) {
		StringBuilder result = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(ALPHANUMERIC_CHARACTERS.length());
			result.append(ALPHANUMERIC_CHARACTERS.charAt(randomIndex));
		}
		return result.toString();
	}

	private boolean isThumbnailExists(String thumbnailPath) {
		try {
			// Check if the file exists in the MinIO bucket
			minioClient.statObject(StatObjectArgs.builder().bucket(thumbnailsBucket).object(thumbnailPath).build());
			return true; // File exists
		} catch (Exception e) {
			return false; // File does not exist
		}
	}

	@Override
	public Video getVideoById(Long id) {
		return videoDao.getVideoById(id);
	}

	@Override
	public List<Video> getAllVideos() {
		return videoDao.getAllVideos();
	}

}
