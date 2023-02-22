package com.example.party.restrictions.exception;

import org.springframework.http.HttpStatus;

import com.example.party.global.exception.BaseException;

public class CheckedBlocksException extends BaseException {
	public CheckedBlocksException(String msg) {
		super(HttpStatus.NOT_FOUND, msg);
	}
}
