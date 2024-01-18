package com.example.party.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CancelApplicationCommand {
	private final Long applicationId;
	private final Long userId;
}
