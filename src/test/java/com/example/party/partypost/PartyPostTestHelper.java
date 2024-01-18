package com.example.party.partypost;

import java.time.LocalDateTime;

import com.example.party.dto.request.CategoryRequest;
import com.example.party.entity.Category;
import com.example.party.dto.request.PartyPostRequest;
import com.example.party.entity.PartyPost;
import com.example.party.user.UserTestHelper;
import com.example.party.entity.User;

public class PartyPostTestHelper {
	public static PartyPost partyPost1() {

		User user = new User(
			UserTestHelper.signupRequest(),
			"encoded password"
		);

		PartyPostRequest partyPostRequest = new PartyPostRequest(
			"title",
			"content",
			1L,
			(byte)3,
			"2023-11-12 16:00",
			"서울특별시 중랑구 망우동",
			"파델라"
		);

		Category category = new Category(new CategoryRequest("category"));

		return new PartyPost(
			user,
			partyPostRequest,
			LocalDateTime.now(),
			category
		);
	}
}
