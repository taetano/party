package com.example.party.partypost.dto;

import java.time.LocalDateTime;

import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.type.Status;

import lombok.Getter;

// 검색결과 , 핫한 모집글 조회결과
@Getter
public class SearchPartyPostListResponse {

	//postId
	private final Long postId;
	//title
	private final String title;
	//status
	private final Status status;
	//maxMember
	private final byte maxMember;
	//partyDate(모임시작시간)
	private final LocalDateTime partyDate;
	//closeDate(모집마감시간)
	private final LocalDateTime closeDate;
	//eubMyeonDong 읍면동

	public SearchPartyPostListResponse(PartyPost partyPost) {
		this.postId = partyPost.getId();
		this.title = partyPost.getTitle();
		this.status = partyPost.getStatus();
		this.maxMember = partyPost.getMaxMember();
		this.partyDate = partyPost.getPartyDate();
		this.closeDate = partyPost.getCloseDate();
	}
}
