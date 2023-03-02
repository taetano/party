package com.example.party.partypost.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.party.application.dto.ApplicationResponse;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.type.Status;

import lombok.Getter;

@Getter
public class MyPartyPostListResponse {

	private final Long id; // partypost id
	private final Long ownerUserId;// 파티장 id
	private final String title; // 제목
	private final Status status; // 모집글 상태
	private final byte maxMember; //총인원
	private final LocalDateTime partyDate; //모임날짜
	private final LocalDateTime closeDate; //마감날짜
	private final String address; //주소
	private final List<ApplicationResponse> joinMember; //참가한 맴버리스트

	public MyPartyPostListResponse(PartyPost partyPost) {
		this.id = partyPost.getId();
		this.ownerUserId = partyPost.getUser().getId();
		this.title = partyPost.getTitle();
		this.status = partyPost.getStatus();
		this.maxMember = partyPost.getMaxMember();
		this.partyDate = partyPost.getPartyDate();
		this.closeDate = partyPost.getCloseDate();
		this.address = partyPost.getAddress();
		this.joinMember = partyPost.getApplications().stream().map(
			ApplicationResponse::new).collect(Collectors.toList());
	}
}
