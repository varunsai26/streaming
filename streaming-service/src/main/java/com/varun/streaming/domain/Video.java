package com.varun.streaming.domain;

import java.util.List;

import lombok.Data;

@Data
public class Video {

	private Long id;
	private Long userId;
	private String name;
	private String description;
	private String thumbnail;
	private Double duration;
	private List<Quality> qualities;
}
