package com.example.party.exception;

import org.springframework.http.HttpStatus;

public class EmailOverlapException extends BaseException {

	public static final String MSG = "해당 이메일이 이미 존재합니다";

	public EmailOverlapException() {
		super(HttpStatus.BAD_REQUEST, MSG);

	}
}