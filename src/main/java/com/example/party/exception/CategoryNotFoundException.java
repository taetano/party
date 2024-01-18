package com.example.party.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends BaseException {

	public static final String MSG = "존재하지 않는 카테고리입니다.";

	public CategoryNotFoundException() {
		super(HttpStatus.NOT_FOUND, MSG);
	}
}
