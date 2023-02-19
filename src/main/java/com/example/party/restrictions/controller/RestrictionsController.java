package com.example.party.restrictions.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.party.global.dto.ListResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.restrictions.dto.ReportNoShowRequest;
import com.example.party.restrictions.dto.ReportPostRequest;
import com.example.party.restrictions.dto.ReportUserRequest;
import com.example.party.restrictions.service.RestrictionsService;
import com.example.party.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restrictions")
public class RestrictionsController {

	private final RestrictionsService restrictionsService;

	//차단등록
	@PostMapping("/block/{userId}")
	public ResponseEntity<ResponseDto> blockUser(@PathVariable Long userId,
		@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(restrictionsService.blockUser(userId, user));
	}

	//차단해제
	@DeleteMapping("/block/{userId}")
	public ResponseEntity<ResponseDto> unBlockUser(@PathVariable Long userId,
		@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(restrictionsService.unBlockUser(userId, user));
	}

	//차단리스트 조회
	@GetMapping("/block")
	public ResponseEntity<ListResponseDto<?>> getBLockedUsers(@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(restrictionsService.blocks(user));
	}

	//유저 신고
	@PostMapping("/report/users")
	public ResponseEntity<ResponseDto> usersReport(@RequestBody ReportUserRequest request,
		@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(restrictionsService.reportUsers(user, request));
	}

	//게시글 신고
	@PostMapping("/report/party-posts")
	public ResponseEntity<ResponseDto> postsReport(@RequestBody ReportPostRequest request,
		@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(restrictionsService.reportPosts(user, request));
	}

	//노쇼 신고
	@PostMapping("/report/noShow")
	public ResponseEntity<ResponseDto> noShowReport(@RequestBody ReportNoShowRequest request,
		@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(restrictionsService.reportNoShow(user, request));
	}
}
