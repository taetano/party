package com.example.party.user.service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.user.dto.LoginRequest;
import com.example.party.user.dto.ProfileResponseDto;
import com.example.party.user.dto.SignupReqest;
import com.example.party.user.entity.User;

public interface IUserService {
	ResponseDto signUp(SignupReqest signupReqest);
	ResponseDto signIn(LoginRequest loginRequest);
	ResponseDto signOut(User user);
	ResponseDto withdraw(User user);
	DataResponseDto<ProfileResponseDto> updateProfile(); // 예시
	DataResponseDto<ProfileResponseDto> findProfile(); // 상대방 프로필 조회
}
