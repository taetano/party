package com.example.party.user.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.party.global.dto.ResponseDto;
import com.example.party.global.security.JwtUserDetailsService;
import com.example.party.user.dto.ProfileRequest;
import com.example.party.user.repository.ProfileRepository;
import com.example.party.user.repository.UserRepository;
import com.example.party.user.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private ProfileRepository profileRepository;
	@InjectMocks
	private UserService userService;

	@Test
	@DisplayName("프로필 내용 수정")
	void updateProfile() {
		//given
		ProfileRequest profileRequest = ProfileRequest.builder()
			.nickName("김홍길동")
			.phoneNum("010-1234-4321")
			.proFileUrl("00")
			.comment("프로필 내용을 수정합니다.")
			.build();

		when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
		when(profileRepository.findById(any(Long.class))).thenReturn(Optional.empty());

		ResponseDto responseDto = userService.updateProfile(profileRequest, anyLong());


	}
}