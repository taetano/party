package com.example.party.restriction.dto;

import com.example.party.restriction.entity.Blocks;

import lombok.Getter;

@Getter
public class BlockResponse {
	private final String blockEmail;

	public BlockResponse(Blocks blocks) {
		this.blockEmail = blocks.getBlocked().getEmail();
	}
}
