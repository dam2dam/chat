package com.example.chattest.util;

import com.example.chattest.dto.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private Utils() {
	}

	public static Message getObject(String message) throws Exception {
		return objectMapper.readValue(message, Message.class);
	}

	public static String getString(Message message) throws Exception {
		return objectMapper.writeValueAsString(message);
	}
}
