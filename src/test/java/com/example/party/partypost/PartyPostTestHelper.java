package com.example.party.partypost;

import java.time.LocalDateTime;

import com.example.party.category.dto.CategoryRequest;
import com.example.party.category.entity.Category;
import com.example.party.partypost.dto.PartyPostRequest;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.user.dto.SignupRequest;
import com.example.party.user.entity.User;

public class PartyPostTestHelper {
	public static PartyPost partyPost1() {
		SignupRequest signupRequest = new SignupRequest(
			"test@email.com",
			"password1!",
			"nickname",
			"010-1234-1234"
		);

		User user = new User(
			signupRequest,
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

		PartyPost partyPost = new PartyPost(
			user,
			partyPostRequest,
			LocalDateTime.now(),
			category
		);
		return partyPost;
	}
}
