package com.example.party.user.dto;

import com.example.party.user.entity.Profile;
import com.example.party.user.entity.User;
import com.example.party.user.repository.ProfileRepository;

import lombok.Getter;

@Getter
public class ProfileResponse { // 예시
	private String email; //user -
	private String nickName; //user -
	private String phoneNum; //user -
	private String proFileUrl; //profile
	private String comment; //profile
	private int participationCount; //profile

	public ProfileResponse(ProfileResponse profileResponse) {
		this.email = profileResponse.getEmail();
		this.nickName = profileResponse.getNickName();
		this.phoneNum = profileResponse.getPhoneNum();
		this.proFileUrl = profileResponse.getProFileUrl();
		this.comment = profileResponse.getComment();
		this.participationCount = profileResponse.getParticipationCount();
	}

	public ProfileResponse(User user, Profile profile) {
		this.email = user.getEmail();
		this.nickName = user.getNickname();
		this.phoneNum = user.getPhoneNum();
		this.proFileUrl = profile.getImg();
		this.comment = profile.getComment();
		this.participationCount = profile.getParticipationCnt();
	}




}
