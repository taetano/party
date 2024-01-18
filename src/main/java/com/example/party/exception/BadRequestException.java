package com.example.party.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {

	public BadRequestException(String customMsg) {
		super(HttpStatus.BAD_REQUEST, customMsg);
	}
}
