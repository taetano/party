package com.example.party.chat.dto;

import lombok.Getter;

@Getter
public class MsgRoomResponse {
	private final String roomName;
	private final Long roomId;

	public MsgRoomResponse(String roomName, Long roomId) {
		this.roomName = roomName;
		this.roomId = roomId;
	}
}
