package com.example.chattest.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.example.chattest.dto.ChatMessage;
import com.example.chattest.repository.ChatRoomRepository;
import com.example.chattest.service.ChatService;

import lombok.RequiredArgsConstructor;

// 메세지는 STOMP의 "destination" 헤더를 기반으로 
// @Controller 객체의 @MethodMapping 메서드로 라우팅 됨
// ApplicationDestinationPrefixes : "/pub"
@Controller
@RequiredArgsConstructor
public class ChatController {

	// private final JwtTokenProvider jwtTokenProvider;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatService chatService;

	/**
	 * websocket "/pub/chat"로 들어오는 메시징을 처리
	 */
	@MessageMapping("/chat")
	public void publishMessage(ChatMessage chatMessage) {
		// public void message(ChatMessage chatMessage, @Header("token") String token) {

		// 유효하지 않은 Jwt토큰이 세팅될 경우 websocket을 통해 보낸 메시지는 무시
		// String userId = jwtTokenProvider.getUserIdFromJwt(token);

		// 로그인 정보로 serder 설정
		// chatMessage.setSender(userId);

		// 채팅방 인원 수 설정
		chatMessage.setUserCount(chatRoomRepository.getUserCount(chatMessage.getRoomId()));

		// Websocket에 발행된 메시지를 redis로 발행 (publish)
		chatService.sendChatMessage(chatMessage);
	}
}
