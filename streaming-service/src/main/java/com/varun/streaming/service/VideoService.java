package com.varun.streaming.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.varun.streaming.domain.Video;

public interface VideoService {

	void uploadVideo(MultipartFile file, MultipartFile thumbnail, Double duration, Integer resolution)
			throws Exception;

	Video getVideoById(Long id);

	List<Video> getAllVideos();

}
