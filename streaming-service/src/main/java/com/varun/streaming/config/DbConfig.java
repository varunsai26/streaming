package com.varun.streaming.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@PropertySource("classpath:db.properties")
public class DbConfig {

	@Bean(name = "videosDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.videos")
	public DataSource videosDataSource() {
		return DataSourceBuilder.create().type(com.mchange.v2.c3p0.ComboPooledDataSource.class).build();
	}

	@Bean(name = "videosJdbcTemplate")
	public JdbcTemplate videosJdbcTemplate(@Qualifier("videosDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean(name = "videosNPJdbcTemplate")
	public NamedParameterJdbcTemplate videosNamedParameterJdbcTemplate(
			@Qualifier("videosDataSource") DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	@Bean(name = "videosTransactionManager")
	public PlatformTransactionManager videosTransactionManager(@Qualifier("videosDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
