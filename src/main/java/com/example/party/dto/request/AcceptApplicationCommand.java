package com.example.party.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AcceptApplicationCommand {
	private final Long applicationId;
	private final Long userId;
}
