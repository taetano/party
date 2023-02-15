package com.example.party.application.dto;

import java.time.LocalDateTime;

import com.example.party.application.entity.Application;
import com.example.party.application.type.ApplicationStatus;

import lombok.Getter;

@Getter
public class ApplicationResponse {
	private final Long id;
	private final String nickname;
	private final String profileImg;
	private final int noShowCnt;
	private final LocalDateTime createdAt;
	private final ApplicationStatus status;
	private final boolean cancel;

	public ApplicationResponse(Application application) {
		this.id = application.getId();
		this.nickname = application.getNickname();
		this.profileImg = application.getProfileImg();
		this.noShowCnt = application.getNoShowCnt();
		this.createdAt = application.getCreatedAt();
		this.status = application.getStatus();
		this.cancel = application.isCancel();
	}
}
