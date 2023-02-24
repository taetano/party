package com.example.party.user.service;

import com.example.party.global.common.ApiResponse;
import com.example.party.user.dto.LoginRequest;
import com.example.party.user.dto.ProfilesRequest;
import com.example.party.user.dto.SignupRequest;
import com.example.party.user.dto.WithdrawRequest;
import com.example.party.user.entity.User;

public interface IUserService {

	//회원가입
	ApiResponse signUp(SignupRequest signupRequest);

	//로그인
	String signIn(LoginRequest loginRequest);

	//로그아웃
	ApiResponse signOut(User user);

	//회원탈퇴
	ApiResponse withdraw(User user, WithdrawRequest withdrawRequest);

	//프로필 수정
	ApiResponse updateProfile(ProfilesRequest profilesRequest, User user);

	//내 프로필 조회
	ApiResponse getMyProfile(User user);

	//상대방 프로필 조회
	ApiResponse getOtherProfile(Long id);
}
