package com.example.party.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateApplicationCommand {
	private final Long partyPostId;
	private final Long userId;
}
