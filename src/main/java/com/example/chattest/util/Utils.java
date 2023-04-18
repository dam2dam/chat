package com.example.chattest.util;

import com.example.chattest.dto.RoomMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private Utils() {
	}

	public static RoomMessage getObject(String message) throws Exception {
		return objectMapper.readValue(message, RoomMessage.class);
	}

	public static String getString(RoomMessage roomMessage) throws Exception {
		return objectMapper.writeValueAsString(roomMessage);
	}
}
