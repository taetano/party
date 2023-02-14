package com.example.party.applicant.service;

import static com.example.party.fixture.ApplicationFixture.*;
import static com.example.party.fixture.PartyPostFixture.*;
import static com.example.party.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.party.applicant.entity.Application;
import com.example.party.applicant.repository.ApplicationRepository;
import com.example.party.applicant.type.ApplicationResponse;
import com.example.party.applicant.type.ApplicationStatus;
import com.example.party.global.dto.ListResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.partypost.entity.PartyPost;
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
			Application application = application1();

			given(applicationRepository.findById(anyLong()))
				.willReturn(Optional.of(application));

			//  when
			ResponseDto result = applicationService.cancelApplication(application.getId(), applicants());

			//  then
			verify(applicationRepository).findById(anyLong());
			assertThat(application.isCancel()).isTrue();
			assertThat(result.getCode()).isEqualTo(200);
		}

		@Test
		void getApplications() {
			//  given
			PartyPost partyPost = partyPost();
			Page<Application> applications = applications();
			given(partyPostRepository.findById(anyLong()))
				.willReturn(Optional.of(partyPost));
			given(applicationRepository.findAllByPartyPostAndCancelIsFalse(anyLong(), any(Pageable.class)))
				.willReturn(applications);

			//  when
			ListResponseDto<ApplicationResponse> ret =
				applicationService.getApplications(partyPost.getId(), writer());

			//  then
			verify(partyPostRepository).findById(anyLong());
			assertThat(ret.getData().size()).isEqualTo(2);
		}

		@Test
		void acceptApplication() {
			//  given
			Application application = application1();

			given(applicationRepository.findById(anyLong()))
				.willReturn(Optional.of(application));

			//  when
			assertThat(application.getStatus()).isEqualTo(ApplicationStatus.PENDING);
			applicationService.acceptApplication(application.getId(), writer());

			//  then
			verify(applicationRepository).findById(anyLong());
			assertThat(application.getStatus()).isEqualTo(ApplicationStatus.ACCEPT);
		}

		@Test
		void rejectApplication() {
			//  given
			Application application = application1();

			given(applicationRepository.findById(anyLong()))
				.willReturn(Optional.of(application));

			//  when
			assertThat(application.getStatus()).isEqualTo(ApplicationStatus.PENDING);
			applicationService.rejectApplication(application.getId(), writer());

			//  then
			verify(applicationRepository).findById(anyLong());
			assertThat(application.getStatus()).isEqualTo(ApplicationStatus.REJECT);
		}
	}

	@DisplayName("실패")
	@Nested
	class Fail {
		@Test
		void cancelApplication_FORBIDDEN() {
			//  given
			Application application = application1();

			given(applicationRepository.findById(anyLong()))
				.willReturn(Optional.of(application));

			//  when
			var thrown = assertThatThrownBy(() -> {
				applicationService.cancelApplication(application.getId(), writer());
			});

			//  then
			thrown.isInstanceOf(ResponseStatusException.class)
				.hasMessageContaining("권한이 없습니다.");
			verify(applicationRepository).findById(anyLong());
		}

		@Test
		void getApplications_FORBIDDEN() {
			//  given
			PartyPost partyPost = partyPost();
			given(partyPostRepository.findById(anyLong()))
				.willReturn(Optional.of(partyPost));

			//  when
			var thrown = assertThatThrownBy(() -> {
				applicationService.getApplications(partyPost.getId(), applicants());
			});

			//  then
			thrown.isInstanceOf(ResponseStatusException.class)
				.hasMessageContaining("권한이 없습니다.");
			verify(partyPostRepository).findById(anyLong());
		}

		@Test
		void getApplications_PARTY_POST_NOT_FOUND() {
			//  given
			Long wrongPartyPostId = 9999L;
			given(partyPostRepository.findById(anyLong()))
				.willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "NOT FOUND"));

			//  when
			var thrown = assertThatThrownBy(() -> {
				applicationService.getApplications(wrongPartyPostId, applicants());
			});

			//  then
			thrown.isInstanceOf(ResponseStatusException.class)
				.hasMessageContaining("NOT FOUND");
			verify(partyPostRepository).findById(anyLong());
		}

		@Test
		void acceptApplication_FORBIDDEN() {
			//  given
			Application application = application1();

			given(applicationRepository.findById(anyLong()))
				.willReturn(Optional.of(application));
			//  when
			var thrown = assertThatThrownBy(() -> {
				applicationService.acceptApplication(application.getId(), applicants());
			});

			//  then
			thrown.isInstanceOf(ResponseStatusException.class)
				.hasMessageContaining("권한이 없습니다.");
			verify(applicationRepository).findById(anyLong());
		}

		@Test
		void rejectApplication_FORBIDDEN() {
			//  given
			Application application = application1();
			given(applicationRepository.findById(anyLong()))
				.willReturn(Optional.of(application));

			//  when
			var thrown = assertThatThrownBy(() -> {
				applicationService.rejectApplication(application.getId(), applicants());
			});

			//  then
			thrown.isInstanceOf(ResponseStatusException.class)
				.hasMessageContaining("권한이 없습니다.");
			verify(applicationRepository).findById(anyLong());
		}
	}

}