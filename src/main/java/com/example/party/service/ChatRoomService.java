package com.example.party.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.party.dto.request.CreateRoomRequest;
import com.example.party.dto.response.ChatRoomResponse;
import com.example.party.entity.ChatRoom;
import com.example.party.repository.ChatRoomRepository;
import com.example.party.entity.User;
import com.example.party.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;

	public void createRoom(CreateRoomRequest command, User user) {
		List<User> users =
			userRepository.findAllByNicknameIn(List.of(command.getNickName(), user.getNickname()));

		if (users.size() <= 2) {
			throw new IllegalArgumentException("User not Found");
		}

		ChatRoom chatRoom = new ChatRoom(
			command.getNickName()+ "," + user.getNickname(),
			users
		);

		chatRoomRepository.save(chatRoom);
	}

	public List<ChatRoomResponse> findAllChatRoomByUser(User user) {
		return chatRoomRepository.findAllByUserId(user.getId()).stream()
			.map(each ->
				new ChatRoomResponse(each.getRoomName().replace(user.getNickname(), ""),
					each.getId()))
			.collect(Collectors.toList());
	}

}
