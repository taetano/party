package com.example.party.global.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {

	public BadRequestException(String msg) {
		super(HttpStatus.BAD_REQUEST, msg);
	}
}
