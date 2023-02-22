package com.example.party.partypost.exception;

import org.springframework.http.HttpStatus;

import com.example.party.user.exception.global.exception.BaseException;

public class PartyPostNotFoundException extends BaseException {

	private static final String msg = "존재하지 않는 모집글 입니다.";

	public PartyPostNotFoundException() {
		super(HttpStatus.NOT_FOUND, msg);
	}
}
