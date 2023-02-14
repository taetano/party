package com.example.party.global.dto;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class DataResponseDto<T> extends ResponseDto {
	private final T data;

	public DataResponseDto(int code, String msg, T data) {
		super(code, msg);
		this.data = data;
	}

	public static <T> DataResponseDto<T> ok(String msg, T data) {
		return new DataResponseDto<>(HttpStatus.OK.value(), msg, data);
	}
}
