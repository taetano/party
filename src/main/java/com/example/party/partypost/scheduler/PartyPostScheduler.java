package com.example.party.partypost.scheduler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.party.partypost.repository.PartyPostRepository;
import com.example.party.partypost.repository.PartyRepository;
import com.example.party.restriction.service.RestrictionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class PartyPostScheduler {
	private final RestrictionService restrictionService;
	private final PartyPostRepository partyPostRepository;
	private final PartyRepository partyRepository;
	// 스케쥴러 작동은 하지만, 현재 쿼리 오류가 발생중이여서 잠시 주석처리 해두었습니다.

	// @Scheduled(cron = "0 45 * * * ?")// 실제 업로드용(매 시 45분마다 실행)
	// @Scheduled(cron = "0 0 * * * ?") // 실제 업로드용(매 시 정각마다 실행)
	// @Scheduled(cron = "0 * * * * ?") // 매 1분 간격으로 실행(테스트용)

	// FOUND -> NO_SHOW_REPORTING (모임시작시간이 되면 변경)
	// @Scheduled(cron = "0 * * * * ?") //(테스트용)1분 간격으로 실행 // 배포시 매시 정각마다로 변경 필요
	// private void changeStatusFoundToNoShow() {
	// 	partyPostRepository.changeStatusFoundToNoShow();
	// }

	// FINDING -> PROCESSING (모집마감시간이 됐는데도 아직 FINDING 상태일 때)
	// @Scheduled(cron = "0 * * * * ?") //(테스트용)1분 간격으로 실행 // 배포시 매시 45분 마다로 변경 필요
	// private void changeStatusFindingToProcessing() {
	// 	partyPostRepository.changeStatusFindingToProcessing();
	// }

	// NO_SHOW_REPORTING -> PROCESSING (모임시간 후 1시간 지나면 변경)
	// @Scheduled(cron = "0 * * * * ?") // (테스트용)1분 간격으로 실행 // 배포시 매시 정각 마다로 변경 필요
	// private void changeStatusNoShowToProcessing() {
	// 	partyPostRepository.changeStatusNoShowToProcessing(LocalDateTime.now().minusHours(1));
	// 	restrictionService.checkingNoShow(partyPostRepository.statusEqualProcessing());
	// }
}
