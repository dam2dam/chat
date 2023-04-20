package com.example.chattest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.example.chattest.handler.StompHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker    // Stomp를 사용하기 위해 선언하는 어노테이션
public class WebSocketConfig
	implements WebSocketMessageBrokerConfigurer {    // WebSocketMessageBrokerConfigurer를 상속받아 STOMP로 메시지 처리 방법을 구성

	private final StompHandler stompHandler;

	/**
	 * 클라이언트에서 WebSocket에 접속할 수 있는 endpoint를 지정
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		// ex : ws://localhost:8080/ws
		registry
			.addEndpoint("/ws")
			.setAllowedOrigins("*");
	}

	/**
	 * 메시지를 중간에서 라우팅할 때 사용하는 메시지 브로커를 구성
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		// 해당 주소를 구독하는 클라이언트에게 메시지 전송
		registry.enableSimpleBroker("/sub");

		// 메시지 발행 요청의 prefix. /pub 로 시작하는 메시지만 해당 Broker에서 받아서 처리
		registry.setApplicationDestinationPrefixes("/pub");
	}

	/**
	 * StompHandler가 Websocket 앞단에서 token을 체크할 수 있도록 인터셉터로 설정
	 */
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(stompHandler);
	}
}
