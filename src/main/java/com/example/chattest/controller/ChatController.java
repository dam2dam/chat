package com.example.chattest.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chattest.RedisPublisher;
import com.example.chattest.RedisSubscriber;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChatController {
	private final RedisMessageListenerContainer redisMessageListenerContainer;
	private final RedisPublisher redisPublisher;
	private final RedisSubscriber redisSubscriber;

	private Map<String, ChannelTopic> channels;

	@PostConstruct
	public void init() {
		channels = new HashMap<>();
	}

	@GetMapping
	public Set<String> findAllChannels() {
		return channels.keySet();
	}

	@PostMapping("/{channelId}")
	public void createRoom(@PathVariable String channelId) {
		ChannelTopic channel = new ChannelTopic(channelId);
		redisMessageListenerContainer.addMessageListener(redisSubscriber, channel);
		channels.put(channelId, channel);
	}
}
