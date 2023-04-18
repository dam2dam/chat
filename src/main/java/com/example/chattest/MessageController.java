package com.example.chattest;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.example.chattest.dto.Message;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MessageController {

	private final SimpMessageSendingOperations simpMessageSendingOperations;

	@MessageMapping("/hello")
	public void message(Message message) {
		simpMessageSendingOperations.convertAndSend("/sub/channel/" + message.getChannelId(), message);
	}
}
