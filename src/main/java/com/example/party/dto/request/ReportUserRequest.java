package com.example.party.dto.request;

import com.example.party.enums.ReportReason;

import lombok.Getter;

@Getter
public class ReportUserRequest {
	private Long userId;
	private ReportReason reason;
	private String detailReason;
}
