package com.example.party.dto.response;

import com.example.party.entity.User;

import lombok.Getter;

@Getter
public class MyProfileResponse {
	private final String email; //user
	private final String nickName; //user
	private final String profileImg; //profile
	private final String comment; //profile
	private final int participationCount; //profile

	public MyProfileResponse(User user) {
		this.email = user.getEmail();
		this.nickName = user.getNickname();
		this.profileImg = user.getProfileImg();
		this.comment = user.getComment();
		this.participationCount = user.getParticipationCnt();
	}

}
