package com.example.party.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilesRequest {
	private String nickname; //user
	private String phoneNum; //user
	private String profileImg; //profile
	private String comment; //profile

}
