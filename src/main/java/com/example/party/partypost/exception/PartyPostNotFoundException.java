package com.example.party.partypost.exception;

import org.springframework.http.HttpStatus;

import com.example.party.global.exception.BaseException;

import lombok.Getter;

@Getter
public class PartyPostNotFoundException extends BaseException {
	private static final String msg = "해당 글이 존재하지 않습니다.";

	public PartyPostNotFoundException() {
		super(HttpStatus.NOT_FOUND, msg);
	}
}
