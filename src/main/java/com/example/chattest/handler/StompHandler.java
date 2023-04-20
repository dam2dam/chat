package com.example.chattest.handler;

import java.security.Principal;
import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.example.chattest.dto.ChatMessage;
import com.example.chattest.repository.ChatRoomRepository;
import com.example.chattest.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

	private static final String TOKEN = "token";
	private static final String SIMP_USER = "simpUser";
	private static final String SIMP_SESSION_ID = "simpSessionId";
	private static final String UNKNOWN_USER = "UnknownUser";

	// private final JwtTokenProvider jwtTokenProvider;
	private final ChatService chatService;
	private final ChatRoomRepository chatRoomRepository;

	/**
	 * websocket을 통해 들어온 요청이 처리 되기전 실행
	 */
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {

		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		if (StompCommand.CONNECT == accessor.getCommand()) {
			// Websocket 연결요청 시

			// Websocket 연결시 헤더의 jwt token 검증
			// jwtTokenProvider.validateToken(accessor.getFirstNativeHeader(TOKEN));
			
		} else {
			if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
				// 채팅룸 구독요청 시

				// header정보에서 구독 destination정보를 얻고, roomId를 추출
				String roomId = chatService.getRoomId(
					Optional.ofNullable((String)message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));

				// 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑 (나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
				String sessionId = (String)message.getHeaders().get(SIMP_SESSION_ID);
				chatRoomRepository.setUserEnterInfo(sessionId, roomId);

				// 채팅방 인원수 +1
				chatRoomRepository.plusUserCount(roomId);

				// 클라이언트 입장 메시지를 채팅방에 발송 (redis publish)
				String name = Optional.ofNullable((Principal)message.getHeaders().get(SIMP_USER))
					.map(Principal::getName)
					.orElse(UNKNOWN_USER);

				chatService.sendChatMessage(
					ChatMessage.builder()
						.type(ChatMessage.MessageType.ENTER)
						.roomId(roomId)
						.sender(name).build());

				log.info("SUBSCRIBED {}, {}", name, roomId);

			} else if (StompCommand.DISCONNECT == accessor.getCommand()) {
				// Websocket 연결 종료 시

				// 연결이 종료된 클라이언트 sesssionId로 채팅방 id를 얻는다.
				String sessionId = (String)message.getHeaders().get(SIMP_SESSION_ID);
				String roomId = chatRoomRepository.getUserEnterRoomId(sessionId);

				// 채팅방 인원수 -1
				chatRoomRepository.minusUserCount(roomId);

				// 클라이언트 퇴장 메시지를 채팅방에 발송 (redis publish)
				String name = Optional.ofNullable((Principal)message.getHeaders().get(SIMP_USER))
					.map(Principal::getName)
					.orElse(UNKNOWN_USER);

				chatService.sendChatMessage(
					ChatMessage.builder()
						.type(ChatMessage.MessageType.QUIT)
						.roomId(roomId)
						.sender(name).build());

				// 퇴장한 클라이언트의 roomId 맵핑 정보 삭제
				chatRoomRepository.removeUserEnterInfo(sessionId);

				log.info("DISCONNECTED {}, {}", sessionId, roomId);
			}
		}
		return message;
	}
}
