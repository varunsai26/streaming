package com.varun.streaming.service;

import com.varun.streaming.domain.Video;

public interface ProcessingService {

	void processVideoToHLS(Video video) throws Exception;

}
