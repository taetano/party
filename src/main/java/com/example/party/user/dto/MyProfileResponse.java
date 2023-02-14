package com.example.party.user.dto;

import com.example.party.user.entity.Profile;
import com.example.party.user.entity.User;

import lombok.Getter;

@Getter
public class MyProfileResponse {
	private String email; //user
	private String nickName; //user
	private String phoneNum; //user
	private String proFileUrl; //profile
	private String comment; //profile
	private int participationCount; //profile

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
