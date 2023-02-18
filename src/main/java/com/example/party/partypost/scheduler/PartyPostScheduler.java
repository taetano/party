package com.example.party.partypost.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.party.partypost.repository.PartyPostRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class PartyPostScheduler {
	private final PartyPostRepository partyPostRepository;
	// @Scheduled(cron = "0 45 * * * ?")// 실제 업로드용(매 시 45분마다 실행)
	// @Scheduled(cron = "0 0 * * * ?") // 실제 업로드용(매 시 정각마다 실행)
	// @Scheduled(cron = "0 * * * * ?") // 매 1분 간격으로 실행(테스트용)

	// FOUND -> NO_SHOW_REPORTING (모임시작시간이 되면 변경)
	@Scheduled(cron = "0 * * * * ?") // 매 1분 간격으로 실행(테스트용)
	private void ChangeStatusFoundToNoShow(){
		partyPostRepository.changeStatusFoundToNoShow();
	}

	// FINDING -> END (모집마감시간이 됐는데, 다 안모였을 때)
	//방법 1) application 레퍼지토리에서 count 쿼리를 날려서 해당 postid를 갖고있으면서 ACCEPT된 application이 몇개인가 세서 그것이 partyPost의 maxMember 수와 일치하는경우 FOUND 로 바꾸고, 아닌경우 END로 바꾼다.
	//>> 모집마감시간이 된 모든 PARTYPOST의 POSTID를 갖고있는 APPLICATION을 일일이 다 세야하니 어마어마하게 부하가 갈 것 같음

	//	방법 2) 그냥 PARTYPOST에게 int acceptedMember 값을 주고 application이 ACCEPT 될 때마다 이 값을 올려서 모집마감시간에
	//	acceptedMember+1 = maxmember 인지 확인하고 아닐경우 END 로 보내는게 깔끔하지 않나 생각중입니다.

	// NO_SHOW_REPORTING -> END (모임시간 후 1시간 지나면 변경)
	@Scheduled(cron = "0 * * * * ?") // 매 1분 간격으로 실행(테스트용)
	private void ChangeStatusNoShowToEnd(){
		partyPostRepository.changeStatusNoShowToEnd(LocalDateTime.now().minusHours(1));
	}


}
