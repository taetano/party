package com.example.party.applicant.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.party.applicant.entity.Application;
import com.example.party.applicant.repository.ApplicationRepository;
import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.user.entity.User;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class ApplicationService implements IApplicationService {
	private final ApplicationRepository applicationRepository;

	@Override
	public DataResponseDto<?> createApplication() {
		return null;
	}

	@Override
	public ResponseDto cancelApplication(Long applicationId, User user) {
		Application application = getApplication(applicationId);
		if (!application.canCancel(user.getId())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권환이 없습니다.");
		}
		application.cancel();
		return ResponseDto.ok("참가 신청 취소 완료");
	}

	@Override
	public ListResponseDto<?> getApplications(Long partPostId) {
		return null;
	}

	@Override
	public DataResponseDto<?> acceptApplication(Long applicationId, User user) {
		return null;
	}

	@Override
	public DataResponseDto<?> rejectApplication(Long applicationId, User user) {
		return null;
	}

	@Transactional(readOnly = true)
	public Application getApplication(Long applicationId) {
		return applicationRepository.findById(applicationId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
				"application with id{" + applicationId + "} is not found"));
	}

}
