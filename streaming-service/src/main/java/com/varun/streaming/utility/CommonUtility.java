package com.varun.streaming.utility;

import java.util.function.Function;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonUtility {
	public static <T, R> R getValueOrDefault(T value, Function<T, R> function, R defaultValue) {
		if (function==null) {
			return defaultValue;
		}
		return value != null ? function.apply(value) : defaultValue;
	}

	public static <T, R> R getValueOrDefault(T value, Function<T, R> function) {
		return getValueOrDefault(value, function, null);
	}
}
