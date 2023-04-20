package com.example.chattest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.chattest.dto.ChatRoom;
import com.example.chattest.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

	// private final JwtTokenProvider jwtTokenProvider;
	private final ChatRoomRepository chatRoomRepository;

	/**
	 * 모든 채팅방 목록
	 */
	@GetMapping("/rooms")
	public List<ChatRoom> getAllRoomList() {
		List<ChatRoom> chatRooms = chatRoomRepository.findAllRooms();
		// userCount 정보 설정
		chatRooms.stream().forEach(room -> room.setUserCount(chatRoomRepository.getUserCount(room.getRoomId())));
		return chatRooms;
	}

	/**
	 * 채팅방 생성
	 */
	@PostMapping("/room")
	public ChatRoom createRoom(@RequestParam String userId) {
		// public ChatRoom createRoom(@Header("token") String token) {
		// String userId = jwtTokenProvider.getUserIdFromJwt(token);
		return chatRoomRepository.createRoom(userId);
	}

	/**
	 * 채팅방 조회
	 */
	@GetMapping("/room/{roomId}")
	public ChatRoom getRoomInfo(@PathVariable String roomId) {
		return chatRoomRepository.findRoomById(roomId);
	}
}
