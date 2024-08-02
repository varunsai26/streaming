package com.varun.streaming.dao;

import java.util.List;

import com.varun.streaming.domain.Video;

public interface VideoDao {

	Video insertVideo(Video video);

	Video getVideoById(Long id);

	List<Video> getAllVideos();

}
