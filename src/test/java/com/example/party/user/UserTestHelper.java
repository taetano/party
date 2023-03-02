package com.example.party.user;

import com.example.party.user.dto.LoginRequest;
import com.example.party.user.dto.ProfilesRequest;
import com.example.party.user.dto.SignupRequest;
import com.example.party.user.dto.WithdrawRequest;

public class UserTestHelper {
	public static SignupRequest signupRequest() {
		return new SignupRequest(
			"test@email.com",
			"password1!",
			"nickname"
		);
	}

	public static LoginRequest loginRequest() {
		return new LoginRequest(
			"test@eamil.com",
			"password1!"
		);
	}

	public static WithdrawRequest withdrawRequest() {
		return new WithdrawRequest("password1!");
	}

	public static ProfilesRequest profilesRequest() {
		return new ProfilesRequest(
			"nickname",
			"010-1234-1234",
			"",
			"comment"
		);
	}
}
