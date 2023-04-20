package com.example.chattest.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.example.chattest.dto.ChatMessage;
import com.example.chattest.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final ChatRoomRepository chatRoomRepository;
	private final RedisTemplate redisTemplate;
	private final ChannelTopic channelTopic;

	/**
	 * destination 정보에서 room ID 추출
	 */
	public String getRoomId(String destination) {
		int lastIndex = destination.lastIndexOf('/');
		if (lastIndex != -1) {
			return destination.substring(lastIndex + 1);
		} else {
			return "";
		}
	}

	/**
	 * 채팅방에 메시지 발송
	 */
	public void sendChatMessage(ChatMessage chatMessage) {
		chatMessage.setUserCount(chatRoomRepository.getUserCount(chatMessage.getRoomId()));
		if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {

		} else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {

		}
		redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
	}
}
