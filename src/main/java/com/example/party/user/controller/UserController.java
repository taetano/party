package com.example.party.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.security.JwtUserDetailsService;
import com.example.party.user.dto.ProfileRequest;
import com.example.party.user.dto.MyProfileResponse;
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
	public DataResponseDto<MyProfileResponse> updateProfile(@Validated @RequestBody ProfileRequest profileRequest,
		@AuthenticationPrincipal
		UserDetails userDetails) {
		//UserDetails userDetails 여기 디테일을 써야됨
		Long test_id = 0l;
		return userService.updateProfile(profileRequest, test_id /*userDetails.getId*/);
	}

	@GetMapping("/profile")
	public DataResponseDto<MyProfileResponse> getMyProfile(@AuthenticationPrincipal
	UserDetails userDetails) {
		//UserDetails userDetails 여기 디테일을 써야됨
		Long test_id = 0l;
		return userService.getMyProfile(test_id /*userDetails.getId*/);
	}

	@GetMapping("/profile/{userId}")
	public DataResponseDto<MyProfileResponse> getOtherProfile(@PathVariable Long userId) {
		return userService.getOtherProfile(userId);
	}

}
