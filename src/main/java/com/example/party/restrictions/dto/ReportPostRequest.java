package com.example.party.restrictions.dto;

import lombok.Getter;

@Getter
public class ReportPostRequest {
	private Long postId;
	private ReportResponse response;
	private String Details;
}
