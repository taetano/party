package com.example.party.restrictions.dto;

import com.example.party.restrictions.entity.Block;

import lombok.Getter;

@Getter
public class BlockResponse {
	private final String userEmail;

	public BlockResponse(Block block) {
		this.userEmail = block.getBlocked().getEmail();
	}
}
