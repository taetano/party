package com.example.party.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.global.security.JwtUserDetailsService;
import com.example.party.user.dto.ProfileRequest;
import com.example.party.user.dto.ProfileResponse;
import com.example.party.user.repository.UserRepository;
import com.example.party.user.service.UserService;
import com.example.party.util.JwtProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

	private final UserService userService;
	private final JwtProvider jwtProvider;

	//프로필 정보 수정
	@PatchMapping("/profile")
	public DataResponseDto<ProfileResponse> updateProfile(@Validated @RequestBody ProfileRequest profileRequest,
		@AuthenticationPrincipal
		JwtUserDetailsService jwtUserDetailsService) {

		return userService.updateProfile(profileRequest,
			jwtUserDetailsService.getProfile().getId());
	}

	@GetMapping("/profile")
	public DataResponseDto<ProfileResponse> getMyProfile(@AuthenticationPrincipal
	JwtUserDetailsService jwtUserDetailsService) {
		return userService.getMyProfile(jwtUserDetailsService.getProfile().getId());
	}

	@GetMapping("/profile/{userId}")
	public DataResponseDto<ProfileResponse> getOtherProfile(@PathVariable Long userId) {
		return userService.getOtherProfile(userId);
	}

}
