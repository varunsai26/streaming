package com.varun.streaming.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class MessageConfig {
	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public RabbitTemplate videosRabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rt = new RabbitTemplate();
		rt.setConnectionFactory(connectionFactory);
		rt.setExchange("videos.exchange");
		rt.setMessageConverter(jsonMessageConverter());
		return rt;
	}

	@Bean
	public RabbitTemplate shortsRabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rt = new RabbitTemplate();
		rt.setConnectionFactory(connectionFactory);
		rt.setExchange("shorts.exchange");
		rt.setMessageConverter(jsonMessageConverter());
		return rt;
	}
	
}