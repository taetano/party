package com.example.party.global.exception;

import org.springframework.http.HttpStatus;

public class BedRequestException extends BaseException {

	public BedRequestException(String msg) {
		super(HttpStatus.BAD_REQUEST, msg);
	}
}
