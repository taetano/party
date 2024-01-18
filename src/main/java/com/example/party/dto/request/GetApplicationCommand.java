package com.example.party.dto.request;

import org.springframework.data.domain.Pageable;

import lombok.Getter;

@Getter
public class GetApplicationCommand {
	private final Long partyPostId;
	private final Long userId;
	private final Pageable pageable;

	public GetApplicationCommand(Long partyPostId, Pageable pageable, Long userId) {
		this.partyPostId = partyPostId;
		this.userId = userId;
		this.pageable = pageable;
	}
}
