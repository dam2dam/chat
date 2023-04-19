package com.example.chattest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
		return chatRoomRepository.findAllRooms();
	}

	/**
	 * 채팅방 생성
	 */
	@PostMapping("/room")
	public ChatRoom createRoom() {
		return chatRoomRepository.createRoom();
	}

	/**
	 * 채팅방 조회
	 */
	@GetMapping("/room/{roomId}")
	public ChatRoom getRoomInfo(@PathVariable String roomId) {
		return chatRoomRepository.findRoomById(roomId);
	}
}
