package com.example.chattest;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.example.chattest.dto.RoomMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPublisher {
	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * 설정한 topic 경로로 roomMessage 전송
	 */
	public void publish(ChannelTopic channelTopic, RoomMessage roomMessage) {

		log.info("Topic : {} | Message : {}", channelTopic.getTopic(), roomMessage.getData());

		redisTemplate.convertAndSend(roomMessage.getChannelId(), roomMessage);
	}
}
