package com.varun.streaming.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.varun.streaming.domain.Video;
import com.varun.streaming.service.ProcessingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProcessingVideoListener {

	@Autowired
	private Gson gson;

	@Autowired
	@Qualifier("videosRabbitTemplate")
	private RabbitTemplate template;

	@Autowired
	private ProcessingService processingService;

	@RabbitListener(queues = "videos")
	public void processVideo(@Payload String message, Message rabbitMessage) {
		try {
			log.info("message: {}", message);
			Video video = gson.fromJson(message, Video.class);
			processingService.processVideoToHLS(video);
		} catch (Exception e) {
			// Log the exception
			log.error("Error processing video message: {}", message, e);

		}
	}

	@RabbitListener(queues = "shorts")
	public void processShort(@Payload String message, Message rabbitMessage) {
		try {
			// Process the received message
			// processingService.processVideoToHLS(video);
		} catch (Exception e) {
			// Log the exception
			log.error("Error processing short message: {}", message, e);

		}
	}
}