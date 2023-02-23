package com.example.party.restriction.dto;

import com.example.party.restriction.entity.Blocks;

import lombok.Getter;

@Getter
public class BlockResponse {
	private final Long blockedId; //내가 차단한 유저 Id
	private final String nickname; //내가 차단한 유저 닉네임

	public BlockResponse(Blocks blocks) {
		this.blockedId = blocks.getBlocked().getId();
		this.nickname = blocks.getBlocked().getNickname();
	}
}
