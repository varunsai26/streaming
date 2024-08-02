package com.varun.streaming.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Quality {

	MEDIUM("480", "640x480"), HD("720", "1280x720"), FULL_HD("1080", "1920x1080");

	private String type;
	private String resolution;

	private Quality(String type, String resolution) {
		this.type = type;
		this.resolution = resolution;
	}

	public String getType() {
		return type;
	}

	public String getResolution() {
		return resolution;
	}

	private static final Map<String, Quality> lookupByType = new HashMap<>();
	private static final Map<String, Quality> lookupByResolution = new HashMap<>();

	static {
		for (Quality quality : Quality.values()) {
			lookupByType.put(quality.getType(), quality);
			lookupByResolution.put(quality.getResolution(), quality);
		}
	}

	public static Quality getQualityByType(String type) {
		return lookupByType.get(type);
	}

	public static Quality getQualityByResolution(String resolution) {
		return lookupByResolution.get(resolution);
	}

	public static List<Quality> getQualitiesForResolution(Integer resolution) {
		List<Quality> qualities = new ArrayList<>();

		if (resolution >= 1080) {
			qualities.add(Quality.FULL_HD);
			qualities.add(Quality.HD);
			qualities.add(Quality.MEDIUM);
		} else if (resolution >= 720) {
			qualities.add(Quality.HD);
			qualities.add(Quality.MEDIUM);
		} else if (resolution >= 480) {
			qualities.add(Quality.MEDIUM);
		}

		return qualities;
	}
}
