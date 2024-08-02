package com.varun.streaming.config;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableTransactionManagement
public class DbConfig {

	@Bean(name = "videosDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.videos")
	public DataSource videosDataSource() throws PropertyVetoException {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
		return dataSource;
	}

	@Bean(name = "videosJdbcTemplate")
	public JdbcTemplate videosJdbcTemplate(@Qualifier("videosDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean(name = "videosTransactionManager")
	public PlatformTransactionManager videosTransactionManager(@Qualifier("videosDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
