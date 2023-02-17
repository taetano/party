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

	// 모집마감시간이 되면 모집글의 상태를 모집 마감으로 변경.
	@Scheduled(cron = "0 * * * * ?") // 매 1분 간격으로 실행
	public void CloseFindingStatusPartyPost() {
		System.out.println(LocalDateTime.now()+"  : 1분 지났어");
		partyPostRepository.changeStatusToFoundWhenCloseDate();

	}
}
