package com.example.party.exception;

import org.springframework.http.HttpStatus;

public class PartyPostNotFoundException extends BaseException {

	public static final String MSG = "존재하지 않는 모집글 입니다.";

	public PartyPostNotFoundException() {
		super(HttpStatus.NOT_FOUND, MSG);
	}
}
