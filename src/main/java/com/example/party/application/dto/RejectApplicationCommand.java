package com.example.party.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RejectApplicationCommand {
	private final Long applicationId;
	private final Long userId;
}
