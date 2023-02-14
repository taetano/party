package com.example.party.user.dto;

import com.example.party.user.entity.Profile;
import com.example.party.user.entity.User;

import lombok.Getter;

@Getter
public class OtherProfileResponse {
	private String email; //user
	private String nickName; //user
	private String proFileUrl; //profile
	private String comment; //profile
	private int noshowcnt; //profile
	private int participationCount; //profile

	public OtherProfileResponse(OtherProfileResponse otherProfileResponse) {
		this.email = otherProfileResponse.getEmail();
		this.nickName = otherProfileResponse.getNickName();
		this.proFileUrl = otherProfileResponse.getProFileUrl();
		this.comment = otherProfileResponse.getComment();
		this.noshowcnt = otherProfileResponse.getNoshowcnt();
		this.participationCount = otherProfileResponse.getParticipationCount();
	}

	public OtherProfileResponse(User user, Profile profile) {
		this.email = user.getEmail();
		this.nickName = user.getNickname();
		this.proFileUrl = profile.getImg();
		this.comment = profile.getComment();
		this.noshowcnt = profile.getNoShowCnt();
		this.participationCount = profile.getParticipationCnt();
	}

}
