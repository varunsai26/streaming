package com.varun.streaming.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Configuration
@EnableAsync
public class StreamingConfig {

	@Bean
	public Gson gson() {
		return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	}
}
