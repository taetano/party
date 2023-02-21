package com.example.party.partypost.dto;

import java.time.LocalDateTime;

import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.type.Status;

import lombok.Getter;

// 검색결과 , 핫한 모집글 조회결과
@Getter
public class PartyPostListResponse {

	//postId
	private final Long postId;
	//모집글의 제목
	private final String title;
	//파티장 닉네임
	private final String partyOwner;
	//모집글의 상태
	private final Status status;
	//현재 인원
	private final byte acceptedMember;
	//총인원
	private final byte maxMember;
	//partyDate(모임시작시간)
	private final LocalDateTime partyDate;
	//closeDate(모집마감시간)
	private final LocalDateTime closeDate;
	//모임 주소(서울 마포구 연남동) 까지만
	private final String partyAddress;
	//모임 장소 (파델라)
	private final String partyPlace;



	public PartyPostListResponse(PartyPost partyPost) {
		this.postId = partyPost.getId();
		this.title = partyPost.getTitle();
		this.partyOwner = partyPost.getUser().getNickname();
		this.status = partyPost.getStatus();
		this.acceptedMember = partyPost.getAcceptedMember();
		this.maxMember = partyPost.getMaxMember();
		this.partyDate = partyPost.getPartyDate();
		this.closeDate = partyPost.getCloseDate();
		this.partyAddress = partyPost.getAddress();
		this.partyPlace = partyPost.getPartyPlace();
	}
}
