package com.example.party.service;

import com.example.party.common.ApiResponse;
import com.example.party.common.DataApiResponse;
import com.example.party.dto.request.*;
import com.example.party.dto.response.ApplicationResponse;
import com.example.party.entity.Application;
import com.example.party.entity.PartyPost;
import com.example.party.entity.User;
import com.example.party.exception.ForbiddenException;
import com.example.party.repository.ApplicationRepository;
import com.example.party.repository.PartyPostRepository;
import com.example.party.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {
    @Mock
    ApplicationRepository applicationRepository;
    @Mock
    PartyPostRepository partyPostRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ApplicationValidator applicationValidator;
    @InjectMocks
    ApplicationService applicationService;

    @DisplayName("성공케이스")
    @Nested
    public class Success {
        @DisplayName("모집글에 참가 신청")
        @Test
        void createApplication() {
            // given
            CreateApplicationCommand mockCreateApplicationCommand = mock(CreateApplicationCommand.class);
            given(mockCreateApplicationCommand.getUserId()).willReturn(1L);
            given(mockCreateApplicationCommand.getPartyPostId()).willReturn(1L);
            User mockUser = mock(User.class);
            given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser));
            PartyPost mockPartyPost = mock(PartyPost.class);
            given(partyPostRepository.findById(anyLong())).willReturn(Optional.of(mockPartyPost));
            willDoNothing().given(applicationValidator).validationApplicationBeforeCreation(any(PartyPost.class),
                    any(User.class));
            // when
            ApiResponse result = applicationService.createApplication(mockCreateApplicationCommand);
            //then
            then(userRepository).should().findById(anyLong());
            then(partyPostRepository).should().findById(anyLong());
            then(applicationValidator).should().validationApplicationBeforeCreation(any(PartyPost.class), any(User.class));
            then(applicationRepository).should().save(any(Application.class));
            assertThat(result.getMsg()).isEqualTo("참가 신청 완료");
            assertThat(result.getCode()).isEqualTo(200);
        }

        @DisplayName("참가신청 취소")
        @Test
        void cancelApplication() {
            // given
            CancelApplicationCommand mockCancelApplicationCommand = mock(CancelApplicationCommand.class);
            given(mockCancelApplicationCommand.getApplicationId()).willReturn(1L);
            Application mockApplication = mock(Application.class);
            given(applicationRepository.findById(anyLong())).willReturn(Optional.of(mockApplication));
            given(mockApplication.isWrittenByMe(anyLong())).willReturn(true);
            // when
            ApiResponse result = applicationService.cancelApplication(mockCancelApplicationCommand);
            //then
            then(applicationRepository).should().findById(anyLong());
            assertThat(result.getMsg()).isEqualTo("참가 신청 취소 완료");
            assertThat(result.getCode()).isEqualTo(200);
        }

        @DisplayName("모집글의 참가신청 목록 조회")
        @Test
        void getApplications() {
            // given
            Pageable mock = mock(Pageable.class);
            GetApplicationCommand mockGetApplicationCommand = mock(GetApplicationCommand.class);
            given(mockGetApplicationCommand.getPartyPostId()).willReturn(1L);
            given(mockGetApplicationCommand.getPageable()).willReturn(mock);
            PartyPost mockPartyPost = mock(PartyPost.class);
            given(mockPartyPost.isWrittenByMe(anyLong())).willReturn(true);
            given(partyPostRepository.findById(anyLong())).willReturn(Optional.of(mockPartyPost));
            Page<Application> mockPage = mock(Page.class);
            given(applicationRepository.findAllByPartyPostAndCancelIsFalse(
                    any(PartyPost.class), any(Pageable.class))).willReturn(mockPage);
            List<Application> mockApplications = mock(List.class);
            given(mockApplications.size()).willReturn(0);
            given(mockPage.getContent()).willReturn(mockApplications);
            // when
            DataApiResponse<ApplicationResponse> result =
                    applicationService.getApplications(mockGetApplicationCommand);
            //then
            then(partyPostRepository).should().findById(anyLong());
            then(applicationRepository).should().findAllByPartyPostAndCancelIsFalse(any(PartyPost.class),
                    any(Pageable.class));
            assertThat(result.getMsg()).isEqualTo("참가신청자 목록 조회 완료");
            assertThat(result.getCode()).isEqualTo(200);
        }

        @DisplayName("참가신청 수락")
        @Test
        void acceptApplication() {
            // given
            AcceptApplicationCommand mockCommand = mock(AcceptApplicationCommand.class);
            Application mockApplication = mock(Application.class);
            given(applicationRepository.findById(anyLong())).willReturn(Optional.of(mockApplication));
            given(mockApplication.isSendByMe(anyLong())).willReturn(true);
            willDoNothing().given(applicationValidator).validateApplicationStatus(any(Application.class));
            // when
            ApiResponse result = applicationService.acceptApplication(mockCommand);
            //then
            then(applicationRepository).should().findById(anyLong());
            then(applicationValidator).should().validateApplicationStatus(any(Application.class));
            assertThat(result.getMsg()).isEqualTo("참가 신청 수락 완료");
            assertThat(result.getCode()).isEqualTo(200);
        }

        @DisplayName("참가신청 거부")
        @Test
        void rejectApplication() {
            // given
            RejectApplicationCommand mockCommand = mock(RejectApplicationCommand.class);
            Application mockApplication = mock(Application.class);
            given(applicationRepository.findById(anyLong())).willReturn(Optional.of(mockApplication));
            given(mockApplication.isSendByMe(anyLong())).willReturn(true);
            willDoNothing().given(applicationValidator).validateApplicationStatus(any(Application.class));
            // when
            ApiResponse result = applicationService.rejectApplication(mockCommand);
            //then
            then(applicationValidator).should().validateApplicationStatus(any(Application.class));
            assertThat(result.getMsg()).isEqualTo("참가 신청 거부 완료");
            assertThat(result.getCode()).isEqualTo(200);
        }

        @DisplayName("단일 참가신청 객체 조회")
        @Test
        void getApplication() {
            // given
            Application mockApplication = mock(Application.class);
            given(applicationRepository.findById(anyLong())).willReturn(Optional.of(mockApplication));
            // when
            Application application = applicationService.getApplication(anyLong());
            //then
            assertThat(application).isEqualTo(mockApplication);
        }
    }

    @DisplayName("실패케이스")
    @Nested
    public class Fail {
        @DisplayName("제3자의 참가신청 취소시 오류")
        @Test
        void cancelApplication() {
            // given
            CancelApplicationCommand mockCancelApplicationCommand = mock(CancelApplicationCommand.class);
            given(mockCancelApplicationCommand.getApplicationId()).willReturn(1L);
            Application mockApplication = mock(Application.class);
            given(applicationRepository.findById(anyLong())).willReturn(Optional.of(mockApplication));
            given(mockApplication.isWrittenByMe(anyLong())).willReturn(false);
            // when
            var thrown =assertThatThrownBy(() -> applicationService.cancelApplication(mockCancelApplicationCommand));
            //then
            then(applicationRepository).should().findById(anyLong());
            thrown.isInstanceOf(ForbiddenException.class)
                    .hasMessage(ForbiddenException.MSG);
        }

        @DisplayName("제3자의 참가신청 목록 조회시 오류")
        @Test
        void getApplications() {
            // given
            GetApplicationCommand mockGetApplicationCommand = mock(GetApplicationCommand.class);
            given(mockGetApplicationCommand.getPartyPostId()).willReturn(1L);
            PartyPost mockPartyPost = mock(PartyPost.class);
            given(mockPartyPost.isWrittenByMe(anyLong())).willReturn(false);
            given(partyPostRepository.findById(anyLong())).willReturn(Optional.of(mockPartyPost));
            // when
            var thrown =assertThatThrownBy(() ->
                    applicationService.getApplications(mockGetApplicationCommand));

            //then
            then(partyPostRepository).should().findById(anyLong());
            thrown.isInstanceOf(ForbiddenException.class)
                    .hasMessage(ForbiddenException.MSG);
        }

        @DisplayName("참가신청 수락시 내가 작성한 모집글이 아닐시 오류")
        @Test
        void acceptApplication() {
            // given
            AcceptApplicationCommand mockCommand = mock(AcceptApplicationCommand.class);
            Application mockApplication = mock(Application.class);
            given(applicationRepository.findById(anyLong())).willReturn(Optional.of(mockApplication));
            given(mockApplication.isSendByMe(anyLong())).willReturn(false);
            // when
            var thrown =assertThatThrownBy(() ->
                    applicationService.acceptApplication(mockCommand));
            //then
            then(applicationRepository).should().findById(anyLong());
            thrown.isInstanceOf(ForbiddenException.class)
                    .hasMessage(ForbiddenException.MSG);
        }
    }
}