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
	private String nickname; //user
	private String profileImg; //profile
	private String comment; //profile

}
