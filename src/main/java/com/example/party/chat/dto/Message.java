package com.example.party.chat.dto;

import lombok.Getter;


@Getter
public class Message {
	public enum MessageType {
		SEND,
		OUT
	}

	private final MessageType messageType;
	private String msg;
	private final String roomId;
	private final String writer;

	public Message(String msg, String messageType, String roomId, String writer) {
		this.msg = msg;
		this.messageType = MessageType.SEND.name().equals(messageType) ? MessageType.SEND : MessageType.OUT;
		this.roomId = roomId;
		this.writer = writer;
	}

	public Message outFromChatRoom(String nickname) {
		this.msg = String.format("%s님이 대화방에서 나가셨습니다.", nickname);
		return this;
	}

	public String getRoomId() {
		return this.roomId;
	}

}
