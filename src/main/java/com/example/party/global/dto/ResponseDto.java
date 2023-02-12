package com.example.party.global.dto;

import lombok.Getter;

@Getter
public class ResponseDto {
	protected final int code;
	protected final String msg;

	public ResponseDto(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
