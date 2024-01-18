package com.example.party.service;

import org.springframework.stereotype.Service;

import com.example.party.entity.Application;
import com.example.party.exception.ApplicationNotAvailableException;
import com.example.party.exception.ApplicationNotGeneraleException;
import com.example.party.repository.ApplicationRepository;
import com.example.party.entity.PartyPost;
import com.example.party.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApplicationValidator {

	public static final String CANNOT_APPLY_TO_MY_OWN_POST = "내가 작성한 모집글에 참가신청할 수 없습니다";
	public static final String CANNOT_APPLY_AFTER_THE_DEADLINE = "모집 마감시간이 지나, 참가신청할 수 없습니다";
	private final ApplicationRepository applicationRepository;

	//참가신청 작성 전 조건 검증 메소드
	public void validationApplicationBeforeCreation(PartyPost partyPost, User user) {
		//(1) 내가 작성자인지 확인
		if (partyPost.isWrittenByMe(user.getId())) {
			throw new ApplicationNotGeneraleException(CANNOT_APPLY_TO_MY_OWN_POST);
		}

		//(2) partyPost 가 모집마감시간전인지 확인
		if (!partyPost.beforeCloseDate()) {
			throw new ApplicationNotGeneraleException(CANNOT_APPLY_AFTER_THE_DEADLINE);
		}
		//(3) partyPost 가 FINDING 인지 확인
		if (!partyPost.isFinding()) {
			throw new ApplicationNotGeneraleException("모집글이 모집 중인 상태가 아닙니다");
		}

		//(4) 중복검사
		if (applicationRepository.existsByPartyPostAndUser(partyPost, user)) {
			throw new ApplicationNotGeneraleException("이미 신청한 모집글입니다");
		}
	}

	//참가신청이 PENDING(대기중) 상태인지 확인
	public void validateApplicationStatus(Application application) {
		if (application.isPending()) {
			throw new ApplicationNotAvailableException();
		}
	}
}
