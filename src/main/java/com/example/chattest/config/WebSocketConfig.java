package com.example.chattest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker    // Stomp를 사용하기 위해 선언하는 어노테이션
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	// private final WebSocketHandler webSocketHandler;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry
			.addEndpoint("/ws")
			.setAllowedOrigins("*");
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/sub");
		registry.setApplicationDestinationPrefixes("/pub");
	}

	// @Override
	// public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
	// 	registry
	// 		// Websocket에 접속하기 위한 endpoint는 /ws/chat으로 설정
	// 		.addHandler(webSocketHandler, "/ws/chat")
	// 		// 다른 서버에서도 접속 가능하도록 CORS : setAllowedOrigins(“*”)를 설정
	// 		.setAllowedOrigins("*");
	// }
}
