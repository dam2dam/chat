package com.example.chattest.dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoom implements Serializable {

	private static final long serialVersionUID = 1L;
	private String roomId;
	private String chiefId;
	private long userCount;

	/**
	 * 채팅방 생성 시 room id 랜덤 생성
	 */
	public static ChatRoom create(String chiefId) {
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.roomId = UUID.randomUUID().toString();
		chatRoom.chiefId = chiefId;
		return chatRoom;
	}
}
