package com.example.party.service;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.party.dto.request.AcceptApplicationCommand;
import com.example.party.dto.response.ApplicationResponse;
import com.example.party.dto.request.CancelApplicationCommand;
import com.example.party.dto.request.CreateApplicationCommand;
import com.example.party.dto.request.GetApplicationCommand;
import com.example.party.dto.request.RejectApplicationCommand;
import com.example.party.entity.Application;
import com.example.party.exception.ApplicationNotFoundException;
import com.example.party.repository.ApplicationRepository;
import com.example.party.common.ApiResponse;
import com.example.party.common.DataApiResponse;
import com.example.party.exception.ForbiddenException;
import com.example.party.entity.PartyPost;
import com.example.party.exception.PartyPostNotFoundException;
import com.example.party.repository.PartyPostRepository;
import com.example.party.entity.User;
import com.example.party.exception.UserNotFoundException;
import com.example.party.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class ApplicationService implements IApplicationService {

	private final ApplicationRepository applicationRepository;
	private final PartyPostRepository partyPostRepository;
	private final UserRepository userRepository;
	private final ApplicationValidator applicationValidator;

	//모집글에 참가 신청
	@Override
	public ApiResponse createApplication(CreateApplicationCommand command) {
		User user = userRepository.findById(command.getUserId())
			.orElseThrow(UserNotFoundException::new);

		PartyPost partyPost = partyPostRepository.findById(command.getPartyPostId())
			.orElseThrow(PartyPostNotFoundException::new);

		applicationValidator.validationApplicationBeforeCreation(partyPost, user);

		Application application = new Application(user, partyPost);

		applicationRepository.save(application);

		return ApiResponse.ok("참가 신청 완료");
	}

	//참가신청 취소
	@Override
	public ApiResponse cancelApplication(CancelApplicationCommand command) {
		Application application = getApplication(command.getApplicationId());

		if (!application.isWrittenByMe(command.getUserId())) {
			throw new ForbiddenException();
		}
		application.cancel();

		return ApiResponse.ok("참가 신청 취소 완료");
	}

	//모집글의 참가신청 목록 조회(파티장만 조회가능)
	@Transactional(readOnly = true)
	@Override
	public DataApiResponse<ApplicationResponse> getApplications(GetApplicationCommand command) {
		PartyPost partyPost = partyPostRepository.findById(command.getPartyPostId())
			.orElseThrow(PartyPostNotFoundException::new);

		if (!partyPost.isWrittenByMe(command.getUserId())) {
			throw new ForbiddenException();
		}

		Page<Application> applications = applicationRepository.findAllByPartyPostAndCancelIsFalse(
			partyPost,
			command.getPageable());

		if (applications.getContent().size() == 0) { // 제거 대상
			return DataApiResponse.ok("참가신청자 목록 조회 완료", Collections.emptyList());
		}

		List<ApplicationResponse> result = applications.map(ApplicationResponse::new).getContent();
		return DataApiResponse.ok("참가신청자 목록 조회 완료", result);
	}

	//(파티장) 참가신청 수락
	@Override
	public ApiResponse acceptApplication(AcceptApplicationCommand command) {
		Application application = getApplication(command.getApplicationId());

		if (!application.isSendByMe(command.getUserId())) {
			throw new ForbiddenException();
		}

		applicationValidator.validateApplicationStatus(application);
		application.accept();
		return ApiResponse.ok("참가 신청 수락 완료");
	}

	//(파티장) 참가신청 거부
	@Override
	public ApiResponse rejectApplication(RejectApplicationCommand command) {
		Application application = getApplication(command.getApplicationId());

		if (!application.isSendByMe(command.getUserId())) {
			throw new ForbiddenException();
		}

		applicationValidator.validateApplicationStatus(application);
		application.reject();

		return ApiResponse.ok("참가 신청 거부 완료");
	}

	//단일 참가신청 객체 불러오기
	@Transactional(readOnly = true)
	public Application getApplication(Long applicationId) {
		return applicationRepository.findById(applicationId)
			.orElseThrow(ApplicationNotFoundException::new);
	}
}
