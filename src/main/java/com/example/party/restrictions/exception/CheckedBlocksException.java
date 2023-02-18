package com.example.party.restrictions.exception;

import org.springframework.http.HttpStatus;

import com.example.party.global.exception.BaseException;

public class CheckedBlocksException extends BaseException {

	private static final String msg = "비어있는 블랙리스트";

	public CheckedBlocksException() {
		super(HttpStatus.NOT_FOUND, msg);
	}
}
