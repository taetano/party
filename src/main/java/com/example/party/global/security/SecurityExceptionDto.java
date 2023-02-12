package com.example.party.global.security;

import lombok.Getter;

@Getter
public class SecurityExceptionDto {
	private final int statusCode;
	private final String statusMsg;

	public SecurityExceptionDto(int statusCode, String statusMsg) {
		this.statusCode = statusCode;
		this.statusMsg = statusMsg;
	}
}
