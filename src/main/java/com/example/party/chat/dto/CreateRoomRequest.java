package com.example.party.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoomRequest {
	private String otherUserName;
}