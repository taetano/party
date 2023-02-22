package com.example.party.restrictions.dto;

import com.example.party.restrictions.entity.PostReport;
import com.example.party.restrictions.type.ReportReason;

import lombok.Getter;

@Getter
public class ReportPostResponse {
	private final String postTitle;
	private final ReportReason response;
	private final String detailReason;

	public ReportPostResponse(PostReport postReport) {
		this.postTitle = postReport.getReportPost().getTitle();
		this.response = postReport.getReason();
		this.detailReason = postReport.getDetailReason();
	}
}
