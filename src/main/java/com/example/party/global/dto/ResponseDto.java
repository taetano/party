package com.example.party.global.dto;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ResponseDto {
	protected final int code;
	protected final String msg;

	public ResponseDto(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static ResponseDto ok(String msg) {
		return new ResponseDto(HttpStatus.OK.value(), msg);
	}

	public static ResponseDto create(String msg) {
		return new ResponseDto(HttpStatus.CREATED.value(), msg);
	}
}
