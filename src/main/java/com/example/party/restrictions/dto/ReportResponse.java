package com.example.party.restrictions.dto;

import com.example.party.restrictions.entity.Report;
import com.example.party.restrictions.type.ReportReason;

import lombok.Getter;

@Getter
public class ReportResponse {
	private final String userEmail;
	private final ReportReason reason;
	private final String details;

	public ReportResponse(Report report) {
		this.userEmail = report.getReportUserEmail();
		this.reason = report.getReason();
		this.details = report.getDetails();
	}
}
