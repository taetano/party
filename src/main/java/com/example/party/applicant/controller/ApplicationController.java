package com.example.party.applicant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.party.applicant.service.ApplicationService;
import com.example.party.global.dto.ResponseDto;
import com.example.party.user.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/applications")
@RestController
public class ApplicationController {
	private final ApplicationService applicationService;

	@PostMapping("/cancel/{applicationId}")
	public ResponseEntity<ResponseDto> cancelApplication(@PathVariable Long applicationId,
		@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(applicationService.cancelApplication(applicationId, user));
	}
}
