package com.example.chattest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.chattest.handler.WebSocketHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket	// Websocket을 활성화
public class WebSocketConfig implements WebSocketConfigurer {
	private final WebSocketHandler webSocketHandler;
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// Websocket에 접속하기 위한 endpoint는 /ws/chat으로 설정
		// 다른 서버에서도 접속 가능하도록 CORS : setAllowedOrigins(“*”)를 설정
		registry.addHandler(webSocketHandler, "/ws/chat").setAllowedOrigins("*");
	}
}
