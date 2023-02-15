package com.example.party.applicant.service;

import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.party.applicant.repository.ApplicationRepository;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.repository.PartyPostRepository;
import com.example.party.user.entity.User;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

	@Mock
	private ApplicationRepository applicationRepository;
	@Mock
	private PartyPostRepository partyPostRepository;
	@Mock
	private User user;
	@InjectMocks
	private ApplicationService applicationService;

	@DisplayName("성공")
	@Nested
	class Success {

		@Test
		void cancelApplication() {
			//  given
			given(applicationRepository.findById(anyLong()))
				.willReturn(Optional.empty());
			//  when
			//  then
		}

		@Test
		void getApplications() {
		}

		@Test
		void acceptApplication() {
		}

		@Test
		void rejectApplication() {
		}

		@Test
		@DisplayName("모집글 생성 성공")
		void createApplication() {
			//given
			PartyPost partyPost = new PartyPost(user, "테스트", "테스트", (byte)5, "평촌동", "경기도 안양시 평촌동",
				"동안로 157 엽기떡볶이",
				LocalDateTime.of(2023, Month.FEBRUARY, 20, 13, 15));

			when(partyPostRepository.findById(anyLong())).thenReturn(Optional.of(partyPost));

			//when
			applicationService.createApplication(1L, user);
			//then
			verify(applicationRepository, times(1));
		}

	}

}