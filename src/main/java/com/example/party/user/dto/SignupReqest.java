package com.example.party.user.dto;

import javax.validation.constraints.Pattern;

import lombok.Getter;

@Getter
public class SignupReqest {
	@Pattern(regexp = "", message = "")
	private String email;
	@Pattern(regexp = "", message = "")
	private String password;
	private String nickname;
	private String phoneNum;
}
