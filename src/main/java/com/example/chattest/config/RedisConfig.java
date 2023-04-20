package com.example.chattest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.example.chattest.service.RedisSubscriber;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private int port;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(host, port);
		// RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		// redisStandaloneConfiguration.setHostName(host);
		// redisStandaloneConfiguration.setPort(port);
		// return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}

	/**
	 * 단일 Topic 사용을 위한 Bean 설정 -> chatroom
	 */
	@Bean
	public ChannelTopic channelTopic() {
		return new ChannelTopic("chatroom");
	}

	/**
	 * 토큰 저장 시 사용
	 */
	@Bean
	public RedisTemplate<?, ?> tokenRedisTemplate() {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		return redisTemplate;
	}

	/**
	 * default 사용
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		return redisTemplate;
	}

	/**
	 * redis에 발행(publish)된 메시지 처리를 위한 리스너 설정
	 */
	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer(
		RedisConnectionFactory redisConnectionFactory,
		MessageListenerAdapter listenerAdapter,
		ChannelTopic channelTopic) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		container.addMessageListener(listenerAdapter, channelTopic);
		return container;
	}

	/**
	 * 실제 메시지를 처리하는 subscriber 설정 추가
	 */
	@Bean
	public MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
		return new MessageListenerAdapter(subscriber, "sendMessage");
	}
}
