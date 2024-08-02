package com.varun.streaming.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.varun.streaming.domain.Video;
import com.varun.streaming.service.VideoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/videos")
public class VideoController {

	@Autowired
	private VideoService videoService;

	@PostMapping("/upload")
	public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file,
			@RequestParam("thumbnail") MultipartFile thumbnail, @RequestParam("duration") Double duration,
			@RequestParam("resolution") Integer resolution) {
		try {
			videoService.uploadVideo(file, thumbnail, duration, resolution);
			return ResponseEntity.status(HttpStatus.CREATED).body("Video uploaded successfully.");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to upload video: " + e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Video> getVideoById(@PathVariable("id") Long id) {
		Video video = videoService.getVideoById(id);
		if (video != null) {
			return ResponseEntity.ok(video);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping
	public ResponseEntity<List<Video>> getAllVideos() {
		List<Video> videos = videoService.getAllVideos();
		return ResponseEntity.ok(videos);
	}
}
