package com.example.party.user.dto;

import com.example.party.user.entity.Profile;
import com.example.party.user.entity.User;

import lombok.Getter;

@Getter
public class MyProfileResponse {
	private final String email; //user
	private final String nickName; //user
	private final String phoneNum; //user
	private final String proFileUrl; //profile
	private final String comment; //profile
	private final int participationCount; //profile

	public MyProfileResponse(MyProfileResponse myProfileResponse) {
		this.email = myProfileResponse.getEmail();
		this.nickName = myProfileResponse.getNickName();
		this.phoneNum = myProfileResponse.getPhoneNum();
		this.proFileUrl = myProfileResponse.getProFileUrl();
		this.comment = myProfileResponse.getComment();
		this.participationCount = myProfileResponse.getParticipationCount();
	}

	public MyProfileResponse(User user, Profile profile) {
		this.email = user.getEmail();
		this.nickName = user.getNickname();
		this.phoneNum = user.getPhoneNum();
		this.proFileUrl = profile.getImg();
		this.comment = profile.getComment();
		this.participationCount = profile.getParticipationCnt();
	}

}
