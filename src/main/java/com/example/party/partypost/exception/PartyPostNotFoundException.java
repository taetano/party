package com.example.party.partypost.exception;

import org.springframework.http.HttpStatus;

import com.example.party.global.exception.BaseException;

public class PartyPostNotFoundException extends BaseException {
	private static final String msg = "모집글이 존재하지 않습니다.";

	public PartyPostNotFoundException() {
		super(HttpStatus.BAD_REQUEST, msg);
	}
}
