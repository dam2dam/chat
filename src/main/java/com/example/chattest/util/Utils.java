package com.example.chattest.util;

import com.example.chattest.dto.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private Utils() {
	}

	public static ChatMessage getObject(String message) throws Exception {
		return objectMapper.readValue(message, ChatMessage.class);
	}

	public static String getString(ChatMessage chatMessage) throws Exception {
		return objectMapper.writeValueAsString(chatMessage);
	}
}
