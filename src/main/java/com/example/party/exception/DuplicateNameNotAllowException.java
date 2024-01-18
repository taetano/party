package com.example.party.exception;

import org.springframework.http.HttpStatus;

public class DuplicateNameNotAllowException extends BaseException {

	public static final String MSG = "카테고리명은 중복이 불가합니다.";

	public DuplicateNameNotAllowException() {
			super(HttpStatus.BAD_REQUEST, MSG);
		}
}
