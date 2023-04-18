package com.example.chattest;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.example.chattest.dto.RoomMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSubscriber implements MessageListener {

	private final SimpMessageSendingOperations simpMessageSendingOperations;
	private final RedisTemplate redisTemplate;
	private final ObjectMapper objectMapper;

	/**
	 * 해당 경로를 구독하고 있으면 메세지를 받을 수 있음
	 */
	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			String body = (String)redisTemplate.getStringSerializer().deserialize(message.getBody());
			RoomMessage roomMessage = objectMapper.readValue(body, RoomMessage.class);

			log.info("Room - message : {}", roomMessage.getData());

			simpMessageSendingOperations.convertAndSend("/sub/channel/" + roomMessage.getChannelId(), roomMessage);
			// redisTemplate.convertAndSend("/sub/channel/" + roomMessage.getChannelId(), roomMessage.getData());

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
