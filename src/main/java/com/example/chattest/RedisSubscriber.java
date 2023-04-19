package com.example.chattest;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.example.chattest.dto.ChatMessage;
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
	 * Redis에서 메시지가 발행(publish)되면 대기하고 있던 onMessage가 해당 메시지를 받아 처리
	 */
	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			// redis에서 발행된 데이터를 받아 deserialize
			String publishMessage = (String)redisTemplate.getStringSerializer().deserialize(message.getBody());
			// ChatMessage 객체로 mapping
			ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);

			log.info("Room - message : {}", chatMessage.getData());

			// subscriber에게 채팅 메시지 Send
			simpMessageSendingOperations.convertAndSend("/sub/chat/room" + chatMessage.getRoomId(), chatMessage);

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
