package com.example.party.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileRequest {
	private String nickName; //user
	private String phoneNum; //user
	private String proFileUrl; //profile
	private String comment; //profile

}
