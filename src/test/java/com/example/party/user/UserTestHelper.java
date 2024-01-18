package com.example.party.user;

import com.example.party.dto.request.LoginCommand;
import com.example.party.dto.request.ProfileRequest;
import com.example.party.dto.request.SignupRequest;
import com.example.party.dto.request.WithdrawRequest;

public class UserTestHelper {
	public static SignupRequest signupRequest() {
		return new SignupRequest(
			"test@email.com",
			"password1!",
			"nickname"
		);
	}

	public static LoginCommand loginRequest() {
		return new LoginCommand(
			"test@eamil.com",
			"password1!"
		);
	}

	public static WithdrawRequest withdrawRequest() {
		return new WithdrawRequest("password1!");
	}

	public static ProfileRequest profilesRequest() {
		return new ProfileRequest(
			"nickname",
			"profileImg",
			"comment"
		);
	}
}
