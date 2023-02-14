package com.example.party.applicant.service;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.party.applicant.repository.ApplicationRepository;
import com.example.party.partypost.repository.PartyPostRepository;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {
	@Mock
	private ApplicationRepository applicationRepository;
	@Mock
	private PartyPostRepository partyPostRepository;
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
	}

}