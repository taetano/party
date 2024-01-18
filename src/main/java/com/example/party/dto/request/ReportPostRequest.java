package com.example.party.dto.request;

import com.example.party.enums.ReportReason;

import lombok.Getter;

@Getter
public class ReportPostRequest {
	private Long postId;
	private ReportReason reason;
	private String detailReason;
}
