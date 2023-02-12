package com.example.party.global.dto;

import lombok.Getter;

@Getter
public class DataResponseDto<T>  extends ResponseDto {
	private final T data;

	public DataResponseDto(int code, String msg, T data) {
		super(code, msg);
		this.data = data;
	}
}
