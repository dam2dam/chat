// package com.example.chattest.handler;
//
// import java.util.Map;
// import java.util.concurrent.ConcurrentHashMap;
//
// import org.springframework.stereotype.Component;
// import org.springframework.web.socket.CloseStatus;
// import org.springframework.web.socket.TextMessage;
// import org.springframework.web.socket.WebSocketSession;
// import org.springframework.web.socket.handler.TextWebSocketHandler;
//
// import com.example.chattest.dto.Message;
// import com.example.chattest.util.Utils;
//
// import lombok.extern.slf4j.Slf4j;
//
// @Component
// @Slf4j
// public class WebSocketHandler extends TextWebSocketHandler {
//
// 	private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
//
// 	/**
// 	 * 웹소켓 연결
// 	 */
// 	@Override
// 	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//
// 		// 세션 저장
// 		String sessionId = session.getId();
// 		sessions.put(sessionId, session);
//
// 		Message message = Message.builder()
// 			.sender(sessionId)
// 			.receiver("all").build();
// 		message.newConnect();
//
// 		// 모든 세션에 알림
// 		sessions.values().forEach(s -> {
// 			try {
// 				if (!s.getId().equals(sessionId)) {
// 					s.sendMessage(new TextMessage(Utils.getString(message)));
// 				}
// 			} catch (Exception e) {
// 				// TODO : throw
// 			}
// 		});
// 	}
//
// 	/**
// 	 * 양방향 데이터 통신
// 	 */
// 	@Override
// 	protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
// 		Message message = Utils.getObject(textMessage.getPayload());
// 		message.setSender(session.getId());
//
// 		// 메시지를 전달할 타겟 찾기
// 		WebSocketSession receiver = sessions.get(message.getReceiver());
// 		// 타겟이 존재하고, 연결 상태라면
// 		if (receiver != null && receiver.isOpen()) {
// 			// 메시지 전송
// 			receiver.sendMessage(new TextMessage(Utils.getString(message)));
// 		}
// 	}
//
// 	/**
// 	 * 웹소켓 연결 종료
// 	 */
// 	@Override
// 	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//
// 		String sessionId = session.getId();
//
// 		// 세션 저장소에서 연결이 끊긴 사용자 삭제
// 		sessions.remove(sessionId);
//
// 		Message message = new Message();
// 		message.closeConnect();
// 		message.setSender(sessionId);
//
// 		sessions.values().forEach(s -> {
// 			try {
// 				s.sendMessage(new TextMessage(Utils.getString(message)));
// 			} catch (Exception e) {
// 				// TODO : throw
// 			}
// 		});
// 	}
//
// 	/**
// 	 * 웹소켓 통신 에러
// 	 */
// 	@Override
// 	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
// 		super.handleTransportError(session, exception);
// 	}
// }
