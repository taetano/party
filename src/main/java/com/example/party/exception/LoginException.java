package com.example.party.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class LoginException extends BaseException {

	public static final String msg = "이메일 또는 비밀번호가 일치하지 않습니다.";

	public LoginException() {
		super(HttpStatus.NOT_FOUND, msg);
	}

}