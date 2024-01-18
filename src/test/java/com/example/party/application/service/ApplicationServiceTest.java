package com.example.party.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import com.example.party.service.ApplicationService;
import com.example.party.service.ApplicationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
import com.example.party.repository.UserRepository;

// TODO: 2023/02/26
// 주최자와 신청참가자로 테스트 출력을 나누는게 좋을 듯.
// 테스트명 naming convention 찾아보기 should, test
@ExtendWith({MockitoExtension.class})
class ApplicationServiceTest {

	@Mock
	private ApplicationRepository applicationRepository;
	@Mock
	private PartyPostRepository partyPostRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private ApplicationValidator applicationValidator;
	@InjectMocks
	private ApplicationService applicationService;

	private Application application;
	private User user;
	private PartyPost partyPost;

	@BeforeEach
	public void setUp() {
		this.application = mock(Application.class);
		this.user = mock(User.class);
		this.partyPost = mock(PartyPost.class);
	}

	@DisplayName("성공")
	@Nested
	class Success {
		@Test
		@DisplayName("모집글에 참가신청을 한다")
		void createApplication() {
			//  given
			CreateApplicationCommand command = mock(CreateApplicationCommand.class);
			User mockedUser = mock(User.class);
			//  when
			when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockedUser));
			when(command.getPartyPostId()).thenReturn(1L);
			when(command.getUserId()).thenReturn(1L);
			when(partyPostRepository.findById(anyLong())).thenReturn(Optional.of(partyPost));
			doNothing().when(applicationValidator)
				.validationApplicationBeforeCreation(any(PartyPost.class), any(User.class));
			when(applicationRepository.save(any(Application.class))).thenReturn(application);

			ApiResponse result = applicationService.createApplication(command);
			//  then
			verify(partyPostRepository).findById(anyLong());
			verify(applicationValidator).validationApplicationBeforeCreation(any(PartyPost.class), any(User.class));
			verify(applicationRepository).save(any(Application.class));
			verify(partyPost).addApplication(any(Application.class));
			assertThat(result.getCode()).isEqualTo(200);
			assertThat(result.getMsg()).isEqualTo("참가 신청 완료");
		}

		@Test
		@DisplayName("모집글에 지원한 참가신청을 취소 한다")
		void cancelApplication() {
			//  given
			CancelApplicationCommand command = mock(CancelApplicationCommand.class);
			//  when
			when(applicationRepository.findById(anyLong())).thenReturn(Optional.of(application));
			when(application.isWrittenByMe(anyLong())).thenReturn(true);

			ApiResponse result = applicationService.cancelApplication(command);

			//  then
			verify(applicationRepository).findById(anyLong());
			verify(application).isWrittenByMe(anyLong());
			verify(application).cancel();
			assertThat(result.getCode()).isEqualTo(200);
			assertThat(result.getMsg()).isEqualTo("참가 신청 취소 완료");
		}

		@Test
		@DisplayName("내가 작성한 모집글에 신청한 참가신청서들을 조회 한다")
		void getApplications() {
			//  given
			Pageable mockedPage = mock(Pageable.class);
			Page<Application> applications = mock(Page.class);
			GetApplicationCommand command = mock(GetApplicationCommand.class);
			//  when
			when(command.getUserId()).thenReturn(1L);
			when(command.getPartyPostId()).thenReturn(1L);
			when(command.getPageable()).thenReturn(mockedPage);
			when(partyPostRepository.findById(anyLong())).thenReturn(Optional.of(partyPost));
			when(partyPost.isWrittenByMe(anyLong())).thenReturn(true);
			when(applicationRepository.findAllByPartyPostAndCancelIsFalse(partyPost, mockedPage))
				.thenReturn(applications);

			DataApiResponse<ApplicationResponse> result = applicationService.getApplications(command);

			//  then
			verify(partyPostRepository).findById(anyLong());
			verify(partyPost).isWrittenByMe(anyLong());
			verify(applicationRepository).findAllByPartyPostAndCancelIsFalse(any(PartyPost.class), any(Pageable.class));
			assertThat(result.getCode()).isEqualTo(200);
			assertThat(result.getMsg()).isEqualTo("참가신청자 목록 조회 완료");
			assertThat(result.getData()).isEmpty();
		}

		@Test
		@DisplayName("내가 작성한 모집글에 신청한 하나의 참가신청서를 참가수락한다")
		void acceptApplication() {
			//  given
			AcceptApplicationCommand command = mock(AcceptApplicationCommand.class);
			//  when
			when(applicationRepository.findById(anyLong())).thenReturn(Optional.of(application));
			when(application.isSendByMe(anyLong())).thenReturn(true);
			doNothing().when(applicationValidator).validateApplicationStatus(any(Application.class));

			ApiResponse result = applicationService.acceptApplication(command);

			//  then
			verify(applicationRepository).findById(anyLong());
			verify(application).accept();
			assertThat(result.getCode()).isEqualTo(200);
			assertThat(result.getMsg()).isEqualTo("참가 신청 수락 완료");
		}

		@Test
		@DisplayName("내가 작성한 모집글에 신청한 하나의 참가신청서를 참가거부한다")
		void rejectApplication() {
			//  given
			RejectApplicationCommand command = mock(RejectApplicationCommand.class);
			//  when
			when(applicationRepository.findById(anyLong())).thenReturn(Optional.of(application));
			when(application.isSendByMe(anyLong())).thenReturn(true);
			doNothing().when(applicationValidator).validateApplicationStatus(any(Application.class));

			ApiResponse result = applicationService.rejectApplication(command);

			//  then
			verify(applicationRepository).findById(anyLong());
			verify(application).isSendByMe(anyLong());
			verify(application).reject();
			assertThat(result.getCode()).isEqualTo(200);
			assertThat(result.getMsg()).isEqualTo("참가 신청 거부 완료");
		}

		@Test
		@DisplayName("내가 작성한 모집글에 신청한 하나의 참가신청서를 조회한다")
		void getApplication() {
			//  given

			//  when
			when(applicationRepository.findById(anyLong())).thenReturn(Optional.of(application));

			applicationService.getApplication(999L);
			//  then
			verify(applicationRepository).findById(anyLong());
		}
	}

	@DisplayName("실패")
	@Nested
	class Fail {
		@Test
		void createApplication_PartyPostNotFoundException() {
		//  given
			User mockedUser = mock(User.class);
			CreateApplicationCommand command = mock(CreateApplicationCommand.class);
			when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockedUser));
		//  when

			var thrown = assertThatThrownBy(() ->
				applicationService.createApplication(command)
			);
			//  then
			verify(partyPostRepository).findById(anyLong());
			thrown.isInstanceOf(PartyPostNotFoundException.class)
				.hasMessageContaining(PartyPostNotFoundException.MSG);
		}

		@Test
		@DisplayName("내가 신청하지 않은 신청서는 취소할 수 없다")
		void cancelApplication_FORBIDDEN() {
			//  given
			CancelApplicationCommand command = mock(CancelApplicationCommand.class);
			//  when
			when(applicationRepository.findById(anyLong())).thenReturn(Optional.of(application));
			when(application.isWrittenByMe(anyLong())).thenReturn(false);

			var thrown = assertThatThrownBy(() -> {
				applicationService.cancelApplication(command);
			});

			//  then
			thrown.isInstanceOf(ForbiddenException.class)
				.hasMessageContaining("허가되지 않은 요청입니다.");
			verify(applicationRepository).findById(anyLong());
		}

		@Test
		void getApplications_PartyPostNotFoundException() {
			//  given
			GetApplicationCommand command = mock(GetApplicationCommand.class);
			//  when
			var thrown = assertThatThrownBy(() -> {
				applicationService.getApplications(command);
			});

			//  then
			thrown.isInstanceOf(PartyPostNotFoundException.class)
				.hasMessageContaining( "존재하지 않는 모집글 입니다.");
			verify(partyPostRepository).findById(anyLong());
		}

		@Test
		void getApplications_FORBIDDEN() {
			//  given
			GetApplicationCommand command = mock(GetApplicationCommand.class);
			//  when
			when(partyPostRepository.findById(anyLong())).thenReturn(Optional.of(partyPost));

			var thrown = assertThatThrownBy(() -> {
				applicationService.getApplications(command);
			});

			//  then
			thrown.isInstanceOf(ForbiddenException.class)
				.hasMessageContaining("허가되지 않은 요청입니다.");
			verify(partyPostRepository).findById(anyLong());
			verify(partyPost).isWrittenByMe(anyLong());
		}

		@Test
		void acceptApplication_FORBIDDEN() {
			//  given
			AcceptApplicationCommand command = mock(AcceptApplicationCommand.class);
			//  when
			when(applicationRepository.findById(anyLong())).thenReturn(Optional.of(application));

			var thrown = assertThatThrownBy(() -> {
				applicationService.acceptApplication(command);
			});

			//  then
			thrown.isInstanceOf(ForbiddenException.class)
				.hasMessageContaining("허가되지 않은 요청입니다.");
			verify(applicationRepository).findById(anyLong());
			verify(application).isSendByMe(anyLong());
		}

		@Test
		void rejectApplication_FORBIDDEN() {
			//  given
			RejectApplicationCommand command = mock(RejectApplicationCommand.class);
			//  when
			when(applicationRepository.findById(anyLong())).thenReturn(Optional.of(application));

			var thrown = assertThatThrownBy(() -> {
				applicationService.rejectApplication(command);
			});

			//  then
			thrown.isInstanceOf(ForbiddenException.class)
				.hasMessageContaining("허가되지 않은 요청입니다.");
			verify(applicationRepository).findById(anyLong());
		}

		@Test
		void getApplication_ApplicationNotFoundException() {
		//  given
		//  when
			var thrown = assertThatThrownBy(() -> {
				applicationService.getApplication(anyLong());
			});
			//  then

			thrown.isInstanceOf(ApplicationNotFoundException.class)
				.hasMessageContaining(ApplicationNotFoundException.MSG);
			verify(applicationRepository).findById(anyLong());
		}

	}

}