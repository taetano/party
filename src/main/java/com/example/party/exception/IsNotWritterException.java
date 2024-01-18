package com.example.party.exception;

import org.springframework.http.HttpStatus;

public class IsNotWritterException extends BaseException {
	public static final String MSG = "해당 회원이 작성한 글이 아닙니다.";

	public IsNotWritterException() {
		super(HttpStatus.FORBIDDEN, MSG);
	}
}
