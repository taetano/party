package com.example.party.restrictions.dto;

import com.example.party.restrictions.entity.UserReport;
import com.example.party.restrictions.type.ReportReason;

import lombok.Getter;

@Getter
public class ReportResponse {
	private final String userEmail;
	private final ReportReason reason;
	private final String details;

	public ReportResponse(UserReport userReport) {
		this.userEmail = userReport.getReported().getEmail();
		this.reason = userReport.getReason();
		this.details = userReport.getDetails();
	}
}
