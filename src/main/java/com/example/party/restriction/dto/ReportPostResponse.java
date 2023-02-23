package com.example.party.restriction.dto;

import com.example.party.restriction.entity.ReportPost;
import com.example.party.restriction.type.ReportReason;

import lombok.Getter;

@Getter
public class ReportPostResponse {
	private final String postTitle;
	private final ReportReason response;
	private final String detailReason;

	public ReportPostResponse(ReportPost reportPost) {
		this.postTitle = reportPost.getReportPost().getTitle();
		this.response = reportPost.getReason();
		this.detailReason = reportPost.getDetailReason();
	}
}
