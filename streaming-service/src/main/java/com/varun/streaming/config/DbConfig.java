package com.varun.streaming.config;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DbConfig {

	@Bean(name = "videosDataSource")
//	@ConfigurationProperties(prefix = "spring.datasource.videos")
	public DataSource videosDataSource() throws PropertyVetoException {
//		ComboPooledDataSource dataSource = DataSourceBuilder.create().type(com.mchange.v2.c3p0.ComboPooledDataSource.class).build();
		  ComboPooledDataSource dataSource = new ComboPooledDataSource();
	        dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
	        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/streaming");
	        dataSource.setUser("root");
	        dataSource.setPassword("123123");

	        dataSource.setInitialPoolSize(3);
	        dataSource.setMinPoolSize(3);
	        dataSource.setMaxPoolSize(10);
	        dataSource.setIdleConnectionTestPeriod(600);
	        dataSource.setMaxIdleTime(1800);
	        dataSource.setAcquireRetryAttempts(4);
	        dataSource.setAcquireRetryDelay(1000);
	        dataSource.setTestConnectionOnCheckin(true);
	        dataSource.setTestConnectionOnCheckout(true);
		log.info("data source :{},{},{},{}",dataSource.getJdbcUrl(),dataSource.getUser(),dataSource.getPassword(),dataSource.getAcquireRetryAttempts());
        return dataSource;
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
