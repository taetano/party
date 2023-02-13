package com.example.party.global.dto;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ListResponseDto<T> extends ResponseDto {
	private final List<T> data;

	public ListResponseDto(int code, String msg, List<T> data) {
		super(code, msg);
		this.data = data;
	}

	public static <T> ListResponseDto<T> ok(String msg, List<T> data) {
		return new ListResponseDto<>(HttpStatus.OK.value(), msg, data);
	}

}
