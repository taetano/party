package com.example.party.partypost.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.repository.PartyRepository;
import com.example.party.restrictions.service.RestrictionsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.party.partypost.repository.PartyPostRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class PartyPostScheduler {
	private final RestrictionsService restrictionsService;
	private final PartyPostRepository partyPostRepository;
	private final PartyRepository partyRepository;
	// @Scheduled(cron = "0 45 * * * ?")// 실제 업로드용(매 시 45분마다 실행)
	// @Scheduled(cron = "0 0 * * * ?") // 실제 업로드용(매 시 정각마다 실행)
	// @Scheduled(cron = "0 * * * * ?") // 매 1분 간격으로 실행(테스트용)

	// FOUND -> NO_SHOW_REPORTING (모임시작시간이 되면 변경)
	@Scheduled(cron = "0 * * * * ?") //(테스트용)1분 간격으로 실행 // 배포시 매시 정각마다로 변경 필요
	private void changeStatusFoundToNoShow() {
		partyPostRepository.changeStatusFoundToNoShow();
	}

	// FINDING -> END (모집마감시간이 됐는데도 아직 FINDING 상태일 때)
	@Scheduled(cron = "0 * * * * ?") //(테스트용)1분 간격으로 실행 // 배포시 매시 45분 마다로 변경 필요
	private void changeStatusFindingToEnd() {
		partyPostRepository.changeStatusFindingToEnd();
	}

	// NO_SHOW_REPORTING -> END (모임시간 후 1시간 지나면 변경)
	@Scheduled(cron = "0 * * * * ?") // (테스트용)1분 간격으로 실행 // 배포시 매시 정각 마다로 변경 필요
	private void changeStatusNoShowToEnd() {
		partyPostRepository.changeStatusNoShowToEnd(LocalDateTime.now().minusHours(1));
		restrictionsService.checkingNoShow(partyPostRepository.statusEqualEnd());
	}
}
