package com.example.party.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ForbiddenException extends BaseException {
	public static final String MSG = "허가되지 않은 요청입니다.";

	public ForbiddenException() {
		super(HttpStatus.FORBIDDEN, MSG);
	}
}
