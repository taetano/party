package com.example.party.application.exception;

import org.springframework.http.HttpStatus;

import com.example.party.global.exception.BaseException;

public class ApplicationNotFoundException extends BaseException {
	private static final String msg = "존재하지 않는 지원자 입니다.";

	public ApplicationNotFoundException() {
		super(HttpStatus.NOT_FOUND, msg);
	}
}
