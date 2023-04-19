package com.example.chattest.handler;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

	private static final String TOKEN = "token";
	// private final JwtTokenProvider jwtTokenProvider;

	/**
	 * websocket을 통해 들어온 요청이 처리 되기전 실행
	 */
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {

		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		// websocket 연결시 헤더의 jwt token 검증
		if (StompCommand.CONNECT == accessor.getCommand()) {
			// jwtTokenProvider.validateToken(accessor.getFirstNativeHeader(TOKEN));
		}
		return message;
	}
}
