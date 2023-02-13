package com.example.party.user.service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.user.dto.LoginRequest;
import com.example.party.user.dto.ProfileResponse;
import com.example.party.user.dto.SignupReqest;
import com.example.party.user.entity.User;

public interface IUserService {
	ResponseDto signUp(SignupReqest signupReqest);
	ResponseDto signIn(LoginRequest loginRequest);
	ResponseDto signOut(User user);
	ResponseDto withdraw(User user);
<<<<<<< HEAD
	DataResponseDto<ProfileResponse> updateProfile(); // 예시
	DataResponseDto<ProfileResponse> findProfile(); // 상대방 프로필 조회
=======
	DataResponseDto<ProfileResponse> updateProfile(); // 예시 , 프로필 수정
	DataResponseDto<ProfileResponse> findProfile(); // 상대방 프로필 조회 , findOtherProfile
	DataResponseDto<ProfileResponse> findMyProfile(); // 내 프로필 조회
>>>>>>> 13c529d3369cf9b75198b58fc9760f1930d27509
}
