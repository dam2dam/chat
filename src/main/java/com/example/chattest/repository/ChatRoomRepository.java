package com.example.chattest.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import com.example.chattest.RedisSubscriber;
import com.example.chattest.dto.ChatRoom;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {

	// 채팅방(topic)에 발행되는 메시지를 처리할 Listner
	private final RedisMessageListenerContainer redisMessageListenerContainer;
	// 구독 처리 서비스
	private final RedisSubscriber redisSubscriber;
	private static final String CHAT_ROOMS = "CHAT_ROOM";
	private final RedisTemplate redisTemplate;
	private HashOperations<String, String, ChatRoom> opsHashChatRoom;

	/**
	 * 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보
	 * 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을 수 있도록 한다
	 */
	private Map<String, ChannelTopic> channelTopics;

	@PostConstruct
	private void init() {
		opsHashChatRoom = redisTemplate.opsForHash();
		channelTopics = new HashMap<>();
	}

	public List<ChatRoom> findAllRooms() {
		return opsHashChatRoom.values(CHAT_ROOMS);
	}

	/**
	 * 채팅방 조회
	 */
	public ChatRoom findRoomById(String roomId) {
		return opsHashChatRoom.get(CHAT_ROOMS, roomId);
	}

	/**
	 * 채팅방 생성
	 */
	public ChatRoom createRoom() {
		ChatRoom chatRoom = ChatRoom.create();
		opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
		return chatRoom;
	}

	/**
	 * 채팅방 입장
	 */
	public void enterRoom(String roodId) {

		ChannelTopic channelTopic = channelTopics.get(roodId);
		if (channelTopic == null) {
			channelTopic = new ChannelTopic(roodId);

			// redis에 ChannelTopic 생성
			channelTopics.put(roodId, channelTopic);
			// pub/sub 통신을 하기 위한 리스너 설정
			redisMessageListenerContainer.addMessageListener(redisSubscriber, channelTopic);
		}
	}

	/**
	 * ChannelTopic 조회
	 */
	public ChannelTopic getTopic(String roomId) {
		return channelTopics.get(roomId);
	}
}
