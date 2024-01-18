package com.example.party.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotActiveException extends BaseException {

	public static final String MSG = "비활성화된 카테고리입니다.";

	public CategoryNotActiveException() {
			super(HttpStatus.BAD_REQUEST, MSG);
	}

}
