package com.example.chattest.controller;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.example.chattest.RedisPublisher;
import com.example.chattest.dto.ChatMessage;
import com.example.chattest.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

	private final SimpMessageSendingOperations simpMessageSendingOperations;

	// private final JwtTokenProvider jwtTokenProvider;

	private final RedisPublisher redisPublisher;
	private final ChatRoomRepository chatRoomRepository;

	@MessageMapping("/chat")
	// public void message(ChatMessage chatMessage) {
	public void message(ChatMessage chatMessage, @Header("token") String token) {

		// 유효하지 않은 Jwt토큰이 세팅될 경우 websocket을 통해 보낸 메시지는 무시
		// String userId = jwtTokenProvider.getUserIdFromJwt(token);

		// 채팅방 입장
		chatRoomRepository.enterRoom(chatMessage.getRoomId());

		// Websocket에 발행된 메시지를 redis로 발행한다(publish)
		redisPublisher.publish(chatRoomRepository.getTopic(chatMessage.getRoomId()), chatMessage);
	}
}
