package com.example.party.restrictions.dto;

import com.example.party.restrictions.type.ReportReason;

import lombok.Getter;

@Getter
public class ReportPostRequest {
	private Long postId;
	private ReportReason reason;
	private String detailReason;
}
