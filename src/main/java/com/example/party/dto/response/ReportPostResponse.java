package com.example.party.dto.response;

import com.example.party.entity.ReportPost;
import com.example.party.enums.ReportReason;

import lombok.Getter;

@Getter
public class ReportPostResponse {
	private final Long id; //신고 이벤트의 Id
	private final Long reportedPostId; //신고된 모집글의 Id
	private final String postTitle;
	private final ReportReason response;
	private final String detailReason;

	public ReportPostResponse(ReportPost reportPost) {
		this.id = reportPost.getId();
		this.reportedPostId = reportPost.getPartyPost().getId();
		this.postTitle = reportPost.getPartyPost().getTitle();
		this.response = reportPost.getReason();
		this.detailReason = reportPost.getDetailReason();
	}
}
