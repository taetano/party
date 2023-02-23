package com.example.party.application.dto;

import java.time.LocalDateTime;

import com.example.party.application.entity.Application;
import com.example.party.application.type.ApplicationStatus;

import lombok.Getter;

@Getter
public class ApplicationResponse {
	private final Long id; // 신청자의 id값
	private final String nickname; // 신청자의 닉네임
	private final String profileImg; // 신청자의 프로필 이미지
	private final int noShowCnt; // 신청자의 노쇼포인트 정보
	private final LocalDateTime createdAt; // 작성된 시간
	private final ApplicationStatus status; // 신청 상태 (PENDING / 	ACCEPT /REJECT )
	private final boolean cancel; // 취소여부

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
