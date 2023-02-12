package com.example.party.user.service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.user.dto.ProfileResponseDto;

public interface IUserService {
	ResponseDto signUp();
	ResponseDto signIn();
	ResponseDto signOut();
	ResponseDto withdraw();
	DataResponseDto<ProfileResponseDto> updateProfile(); // 예시
	DataResponseDto<ProfileResponseDto> findProfile(); // 상대방 프로필 조회
}
