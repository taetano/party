package com.example.party.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateApplicationCommand {
	private final Long partyPostId;
	private final Long userId;
}
