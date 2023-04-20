package com.example.chattest.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ChatMessage implements Serializable {

	private MessageType type;
	private String roomId;
	private String sender;
	private Object data;
	private long userCount;

	/**
	 * 메시지 타입 : 입장, 채팅, 퇴장
	 */
	public enum MessageType {
		ENTER, TALK, QUIT
	}
}
