package com.example.party.category.exception;

import org.springframework.http.HttpStatus;

import com.example.party.user.exception.global.exception.BaseException;

public class CategoryNotActiveException extends BaseException {

	private static final String msg = "비활성화된 카테고리입니다.";

	public CategoryNotActiveException() {
			super(HttpStatus.BAD_REQUEST, msg);
	}

}
