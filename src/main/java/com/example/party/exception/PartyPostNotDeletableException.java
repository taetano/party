package com.example.party.exception;

import org.springframework.http.HttpStatus;

public class PartyPostNotDeletableException extends BaseException {

	public PartyPostNotDeletableException(String customMsg) {
		super(HttpStatus.NOT_FOUND, customMsg);
	}
}
