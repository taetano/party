package com.example.party.chat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.party.chat.dto.CreateRoomRequest;
import com.example.party.chat.dto.MsgRoomResponse;
import com.example.party.chat.entity.MsgRoom;
import com.example.party.chat.repository.MsgRoomRepository;
import com.example.party.user.entity.User;
import com.example.party.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MsgService {
	private final MsgRoomRepository msgRoomRepository;
	private final UserRepository userRepository;

	public void createRoom(CreateRoomRequest command, User user) {
		User other = userRepository.findByNickname(command.getOtherUserName())
			.orElseThrow(() -> new RuntimeException("User Not Found"));

		MsgRoom msgRoom = new MsgRoom(other.getNickname() + user.getNickname(), user, other);

		msgRoomRepository.save(msgRoom);
	}

	public List<MsgRoomResponse> findAllByUserId(User user) {
		return msgRoomRepository.findAllByUserId(user.getId()).stream()
			.map(msgRoom -> new MsgRoomResponse(msgRoom.getRoomName().replace(user.getNickname(), ""), msgRoom.getId()))
			.collect(Collectors.toList());
	}

}
