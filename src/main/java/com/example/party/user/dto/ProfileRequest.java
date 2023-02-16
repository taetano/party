package com.example.party.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileRequest {
	private String nickName; //user
	private String phoneNum; //user
	private String proFileUrl; //profile
	private String comment; //profile

}
