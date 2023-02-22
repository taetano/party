package com.example.party.category.exception;

import org.springframework.http.HttpStatus;

import com.example.party.user.exception.global.exception.BaseException;

public class DuplicateNameNotAllowException extends BaseException {

	private static final String msg = "카테고리명은 중복이 불가합니다.";

	public DuplicateNameNotAllowException() {
			super(HttpStatus.BAD_REQUEST, msg);
		}
}
