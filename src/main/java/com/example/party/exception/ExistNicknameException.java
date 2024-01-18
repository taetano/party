package com.example.party.exception;

import org.springframework.http.HttpStatus;

public class ExistNicknameException extends BaseException {

	public static final String MSG = "중복 닉네임이 존재합니다";

	public ExistNicknameException() {
		super(HttpStatus.BAD_REQUEST, MSG);

	}
}