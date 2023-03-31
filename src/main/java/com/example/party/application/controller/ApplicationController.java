package com.example.party.application.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.party.application.dto.AcceptApplicationCommand;
import com.example.party.application.dto.ApplicationResponse;
import com.example.party.application.dto.CancelApplicationCommand;
import com.example.party.application.dto.CreateApplicationCommand;
import com.example.party.application.dto.GetApplicationCommand;
import com.example.party.application.dto.RejectApplicationCommand;
import com.example.party.application.service.ApplicationService;
import com.example.party.global.common.ApiResponse;
import com.example.party.global.common.DataApiResponse;
import com.example.party.user.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/applications")
@RestController
public class ApplicationController { // Application 테이블의 데이터에 접근하기 위한 엔트리 포인트

	private final ApplicationService applicationService;

	//모집글작성
	@PostMapping("/join/{party-postId}")
	public ResponseEntity<ApiResponse> createApplication(
		final @PathVariable(name = "party-postId") Long partyPostId,
		final @AuthenticationPrincipal User loggedInUser) //
	{
		CreateApplicationCommand createApplicationCommand =
			new CreateApplicationCommand(partyPostId, loggedInUser.getId());
		return ResponseEntity.ok(applicationService.createApplication(createApplicationCommand));
	}

	//모집참가취소
	@PostMapping("/cancel/{applicationId}")
	public ResponseEntity<ApiResponse> cancelApplication(
		final @PathVariable Long applicationId,
		final @AuthenticationPrincipal User loggedInUser) //
	{
		CancelApplicationCommand cancelApplicationCommand =
			new CancelApplicationCommand(applicationId, loggedInUser.getId());
		return ResponseEntity.ok(applicationService.cancelApplication(cancelApplicationCommand));
	}

	//해당 모집글의 참가신청 조회
	@GetMapping("/{party-postId}")
	public ResponseEntity<DataApiResponse<ApplicationResponse>> getApplications(
		final @PageableDefault(sort = "createdAt") Pageable pageable,
		final @PathVariable(name = "party-postId") Long partyPostId,
		final @AuthenticationPrincipal User loggedInUser) //
	{
		GetApplicationCommand getApplicationCommand =
			new GetApplicationCommand(partyPostId, pageable, loggedInUser.getId());
		return ResponseEntity.ok(applicationService.getApplications(getApplicationCommand));
	}

	//(파티장) 참가신청 수락
	@PostMapping("/accept/{applicationId}")
	public ResponseEntity<ApiResponse> acceptApplication(
		final @PathVariable Long applicationId,
		final @AuthenticationPrincipal User loggedInUser) //
	{
		AcceptApplicationCommand acceptApplicationCommand =
			new AcceptApplicationCommand(applicationId, loggedInUser.getId());
		return ResponseEntity.ok(applicationService.acceptApplication(acceptApplicationCommand));
	}

	//(파티장) 참가신청 거부
	@PostMapping("/reject/{applicationId}")
	public ResponseEntity<ApiResponse> rejectApplication(
		final @PathVariable Long applicationId,
		final @AuthenticationPrincipal User loggedInUser) //
	{
		RejectApplicationCommand rejectApplicationCommand =
			new RejectApplicationCommand(applicationId, loggedInUser.getId());
		return ResponseEntity.ok(applicationService.rejectApplication(rejectApplicationCommand));
	}

}
