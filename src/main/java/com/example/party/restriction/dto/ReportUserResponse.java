package com.example.party.restriction.dto;

import com.example.party.restriction.entity.ReportUser;
import com.example.party.restriction.type.ReportReason;

import lombok.Getter;

@Getter
public class ReportUserResponse {
	private final String userEmail;
	private final ReportReason reason;
	private final String detailReason;

	public ReportUserResponse(ReportUser reportUser) {
		this.userEmail = reportUser.getReported().getEmail();
		this.reason = reportUser.getReason();
		this.detailReason = reportUser.getDetailReason();
	}
}
