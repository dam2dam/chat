package com.example.chattest.controller;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.example.chattest.RedisPublisher;
import com.example.chattest.RedisSubscriber;
import com.example.chattest.dto.RoomMessage;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MessageController {

	private final SimpMessageSendingOperations simpMessageSendingOperations;
	private final RedisPublisher redisPublisher;
	private final RedisSubscriber redisSubscriber;
	private final RedisMessageListenerContainer redisMessageListenerContainer;

	@MessageMapping("/chat")
	public void message(RoomMessage roomMessage) {
		// redisMessageListenerContainer.addMessageListener(redisSubscriber, new ChannelTopic(roomMessage.getChannelId()));
		redisPublisher.publish(new ChannelTopic(roomMessage.getChannelId()), roomMessage);
		// simpMessageSendingOperations.convertAndSend("/sub/channel/" + roomMessage.getChannelId(), roomMessage);
	}
}

