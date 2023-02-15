package com.example.party.fixture;

import java.time.LocalDateTime;

import com.example.party.partypost.entity.PartyPost;

public class PartyPostFixture {
	public static PartyPost partyPost() {
		PartyPost partyPost = new PartyPost(
			UserFixture.writer(),
			"title",
			"content",
			(byte)3,
			"읍면동",
			"address",
			"detailAddress",
			LocalDateTime.now()
		);
		partyPost.setId(999L);
		return partyPost;
	}
}
