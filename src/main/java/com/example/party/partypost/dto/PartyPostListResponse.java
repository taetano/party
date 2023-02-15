package com.example.party.partypost.dto;

import java.time.LocalDateTime;

import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.type.Status;

import lombok.Getter;

@Getter
public class PartyPostListResponse {

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
	//address
	private final String address;

	public PartyPostListResponse(PartyPost partyPost) {
		this.title = partyPost.getTitle();
		this.status = partyPost.getStatus();
		this.closeDate = partyPost.getCloseDate();
		this.maxMember = partyPost.getMaxMember();
		this.partyDate = partyPost.getPartyDate();
		this.address = partyPost.getAddress();
	}
}
