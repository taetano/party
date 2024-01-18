package com.example.party.exception;

import org.springframework.http.HttpStatus;

public class ApplicationNotGeneraleException extends BaseException {

	public ApplicationNotGeneraleException(String customMsg) {
		super(HttpStatus.BAD_REQUEST, customMsg);
	}
}
