package com.example.party.dto.response;

import lombok.Getter;

@Getter
public class ChatRoomResponse {
	private final String roomName;
	private final Long roomId;

	public ChatRoomResponse(String roomName, Long roomId) {
		this.roomName = roomName;
		this.roomId = roomId;
	}
}
