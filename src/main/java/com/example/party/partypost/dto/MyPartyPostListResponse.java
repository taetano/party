package com.example.party.partypost.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.party.applicant.type.ApplicationResponse;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.type.Status;

import lombok.Getter;

@Getter
public class MyPartyPostListResponse {

	private final Long id;
	private final String title;
	private final Status status;
	private final byte maxMember;
	private final LocalDateTime partyDate;
	private final LocalDateTime closeDate;
	private final String address;
	private final List<ApplicationResponse> joinMember;

	public MyPartyPostListResponse(PartyPost partyPost) {
		this.id = partyPost.getId();
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
