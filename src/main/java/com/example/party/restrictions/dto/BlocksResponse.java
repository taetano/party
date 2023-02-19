package com.example.party.restrictions.dto;

import com.example.party.restrictions.entity.Block;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class BlocksResponse {
	private final String blockUserEmail;

	public BlocksResponse(Block block) {
		this.blockUserEmail = block.getBlockedEmail();
	}
}
