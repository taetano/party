package com.example.party.exception;

import org.springframework.http.HttpStatus;

public class ApplicationNotAvailableException extends BaseException {
	public static final String MSG = "변경가능한 신청서의 상태가 아닙니다.";

	public ApplicationNotAvailableException() {
		super(HttpStatus.NOT_FOUND, MSG);
	}
}
