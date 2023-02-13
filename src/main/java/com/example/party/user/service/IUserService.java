package com.example.party.user.service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.user.dto.LoginRequest;
import com.example.party.user.dto.ProfileResponse;
import com.example.party.user.dto.SignupRequest;
import com.example.party.user.entity.User;

public interface IUserService {
	//회원가입
	ResponseDto signUp(SignupRequest signupRequest);
	//로그인
	ResponseDto signIn(LoginRequest loginRequest);
	//로그아웃
	ResponseDto signOut(User user);
	//회원탈퇴
	ResponseDto withdraw(User user);
	//프로필 수정
	DataResponseDto<ProfileResponse> updateProfile();
	//내 프로필 조회
	DataResponseDto<ProfileResponse> getMyProfile();
	//상대방 프로필 조회
	DataResponseDto<ProfileResponse> getOtherProfile();
}
