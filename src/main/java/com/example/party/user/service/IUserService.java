package com.example.party.user.service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.user.dto.ProfileResponse;

public interface IUserService {
	ResponseDto signUp();
	ResponseDto signIn();
	ResponseDto signOut();
	ResponseDto withdraw();
	DataResponseDto<ProfileResponse> updateProfile(); // 예시 , 프로필 수정
	DataResponseDto<ProfileResponse> findProfile(); // 상대방 프로필 조회 , findOtherProfile
	DataResponseDto<ProfileResponse> findMyProfile(); // 내 프로필 조회
}
