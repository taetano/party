package com.example.party.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.party.application.dto.ApplicationResponse;
import com.example.party.application.service.ApplicationService;
import com.example.party.global.common.ApiResponse;
import com.example.party.global.common.DataApiResponse;
import com.example.party.user.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/applications")
@RestController
public class ApplicationController {

	private final ApplicationService applicationService;

	@PostMapping("/join/{party-postId}")
	public ResponseEntity<ApiResponse> createApplication(@PathVariable(name = "party-postId") Long partyPostId,
		@AuthenticationPrincipal User user) {

		return ResponseEntity.ok(applicationService.createApplication(partyPostId, user));
	}

	@PostMapping("/cancel/{applicationId}")
	public ResponseEntity<ApiResponse> cancelApplication(@PathVariable Long applicationId,
		@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(applicationService.cancelApplication(applicationId, user));
	}

	@GetMapping("/{party-postId}")
	public ResponseEntity<DataApiResponse<ApplicationResponse>> getApplications(
		@PathVariable(name = "party-postId") Long partyPostId, @AuthenticationPrincipal User user) {
		return ResponseEntity.ok(applicationService.getApplications(partyPostId, user));
	}

	@PostMapping("/accept/{applicationId}")
	public ResponseEntity<ApiResponse> acceptApplication(
		@PathVariable Long applicationId,
		@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(applicationService.acceptApplication(applicationId, user));
	}

	@PostMapping("/reject/{applicationId}")
	public ResponseEntity<ApiResponse> rejectApplication(@PathVariable Long applicationId,
		@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(applicationService.rejectApplication(applicationId, user));
	}

}
