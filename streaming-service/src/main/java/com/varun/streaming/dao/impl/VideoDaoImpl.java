package com.varun.streaming.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.varun.streaming.dao.VideoDao;
import com.varun.streaming.domain.Quality;
import com.varun.streaming.domain.Video;

public class VideoDaoImpl implements VideoDao {

	@Autowired
	@Qualifier("videosNPJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public Video insertVideo(Video video) {
		String sql = "INSERT INTO videos (id, user_id, name, description, qualities, thumbnail) "
				+ "VALUES (:id, :userId, :name, :description, :qualities,:thumbnail)";

		KeyHolder keyHolder = new GeneratedKeyHolder(); // KeyHolder to retrieve generated keys

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", video.getId())
				.addValue("userId", video.getUserId()).addValue("name", video.getName())
				.addValue("description", video.getDescription())
				.addValue("qualities",
						video.getQualities().stream().map(Quality::getType).collect(Collectors.joining(",")))
				.addValue("thumbnail", video.getThumbnail());

		// Perform the insert with key holder
		jdbcTemplate.update(sql, params, keyHolder);

		// Retrieve the generated ID and set it back to the video object
		if (keyHolder.getKey() != null) {
			Long generatedId = keyHolder.getKey().longValue();
			video.setId(generatedId);
		}

		return video;
	}

	public static Video mapVideo(ResultSet rs) throws SQLException {
		Video video = new Video();
		video.setId(rs.getLong("id"));
		video.setUserId(rs.getLong("user_id"));
		video.setName(rs.getString("name"));
		video.setDescription(rs.getString("description"));

		String qualitiesStr = rs.getString("qualities");
		List<Quality> qualities = Arrays.stream(qualitiesStr.split(",")).map(Quality::getQualityByType)
				.collect(Collectors.toList());
		video.setQualities(qualities);
		video.setThumbnail(rs.getString("thumbnail"));

		return video;

	}

	@Override
	public Video getVideoById(Long id) {
		String sql = "SELECT id, name, user_id, description, qualities, thumbnail FROM videos WHERE id = :id";
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
		List<Video> videos = new ArrayList<>();

		jdbcTemplate.query(sql, params, rs -> {
			videos.add(mapVideo(rs));
		});
		return videos.get(0);
	}

	@Override
	public List<Video> getAllVideos() {
		String sql = "SELECT id, name, user_id, description, qualities, thumbnail FROM videos";
		List<Video> videos = new ArrayList<>();

		jdbcTemplate.query(sql, rs -> {
			videos.add(mapVideo(rs));
		});
		return videos;
	}
}
