package com.example.chattest.service;

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
public class RedisSubscriber {

	private final SimpMessageSendingOperations simpMessageSendingOperations;
	private final RedisTemplate redisTemplate;
	private final ObjectMapper objectMapper;

	/**
	 * Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리
	 */
	public void sendMessage(String publishMessage) {
		try {
			// ChatMessage 객체로 mapping
			ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);

			log.info("Room - message : {}", chatMessage.getData());

			// 채팅방을 구독한 클라이언트에게 메시지 발송
			simpMessageSendingOperations.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
			redisTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
