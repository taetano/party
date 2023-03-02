package com.example.party.partypost.dto;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.example.party.application.dto.ApplicationResponse;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.type.Status;

import lombok.Getter;

@Getter
public class PartyPostResponse {

	private final Long id;
	private final Long userId; // 파티장 Id
	private final String nickname;
	private final String title;
	private final String content;
	private final Long categoryId;
	private final String categoryName;
	private final Status status;
	private final byte acceptedMember;
	private final byte maxMember;
	private final LocalDateTime partyDate;
	private final LocalDateTime closeDate;
	private final String day;
	private final String address;
	private final String detailAddress;

	private final String partyPlace;
	private final int viewCnt;
	private final List<ApplicationResponse> joinMember;

	//생성자
	public PartyPostResponse(PartyPost partyPost) {
		this.id = partyPost.getId();
		this.userId = partyPost.getUser().getId();
		this.nickname = partyPost.getUser().getNickname();
		this.title = partyPost.getTitle();
		this.content = partyPost.getContent();
		this.categoryId = partyPost.getCategory().getId();
		this.categoryName = partyPost.getCategory().getName();
		this.viewCnt = partyPost.getViewCnt();
		this.partyPlace = partyPost.getPartyPlace();
		this.acceptedMember = partyPost.getAcceptedMember();
		this.status = partyPost.getStatus();
		this.maxMember = partyPost.getMaxMember();
		this.partyDate = partyPost.getPartyDate();
		this.closeDate = partyPost.getCloseDate();
		this.day = partyDate.getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREAN);
		this.address = partyPost.getAddress();
		this.detailAddress = partyPost.getDetailAddress();
		this.joinMember = partyPost.getApplications().stream().map(
			ApplicationResponse::new).collect(Collectors.toList());
	}
}
