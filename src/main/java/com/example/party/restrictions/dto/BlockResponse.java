package com.example.party.restrictions.dto;

import com.example.party.restrictions.entity.Blocks;

import lombok.Getter;

@Getter
public class BlockResponse {
	private final String blockEmail;

	public BlockResponse(Blocks blocks) {
		this.blockEmail = blocks.getBlocked().getEmail();
	}
}
