package com.example.party.service;

import com.example.party.entity.Application;
import com.example.party.entity.PartyPost;
import com.example.party.entity.User;
import com.example.party.exception.ApplicationNotAvailableException;
import com.example.party.exception.ApplicationNotGeneraleException;
import com.example.party.repository.ApplicationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ApplicationValidatorTest {
    @Mock
    ApplicationRepository applicationRepository;
    @InjectMocks
    ApplicationValidator applicationValidator;

    @DisplayName("성공케이스")
    @Nested
    public class Success {
        @DisplayName("참가신청 조건 검증")
        @Test
        void validationApplicationBeforeCreation() {
            // given
            PartyPost mockPartyPost = mock(PartyPost.class);
            given(mockPartyPost.isWrittenByMe(anyLong())).willReturn(false);
            given(mockPartyPost.beforeCloseDate()).willReturn(true);
            given(mockPartyPost.isFinding()).willReturn(true);
            User mockUser = mock(User.class);
            given(mockUser.getId()).willReturn(1L);
            given(applicationRepository.existsByPartyPostAndUser(any(PartyPost.class), any(User.class))).willReturn(false);
            // when
            applicationValidator.validationApplicationBeforeCreation(mockPartyPost, mockUser);
            //then
            then(applicationRepository).should().existsByPartyPostAndUser(any(PartyPost.class), any(User.class));
            then(mockPartyPost).should().isWrittenByMe(anyLong());
            then(mockPartyPost).should().beforeCloseDate();
            then(mockPartyPost).should().isFinding();
        }

        @DisplayName("참가신청중인지 검사 (중복, 승인,삭제 전 확인)")
        @Test
        void validateApplicationStatus() {
            // given
            Application mockApplication = mock(Application.class);
            given(mockApplication.isPending()).willReturn(false);
            // when
            applicationValidator.validateApplicationStatus(mockApplication);
            //then
            then(mockApplication).should().isPending();
        }
    }

    @DisplayName("실패케이스")
    @Nested
    public class Fail {
         @DisplayName("내가 작성한 글에 신청시 오류")
         @Test
         void validationApplicationBeforeCreation_isWrittenByMe() {
             // given
             PartyPost mockPartyPost = mock(PartyPost.class);
             given(mockPartyPost.isWrittenByMe(anyLong())).willReturn(true);
             User mockUser = mock(User.class);
             given(mockUser.getId()).willReturn(1L);
             // when
             var thrown =
                     assertThatThrownBy(() -> applicationValidator.validationApplicationBeforeCreation(mockPartyPost, mockUser));
             //then
             then(mockPartyPost).should().isWrittenByMe(anyLong());
             thrown.isInstanceOf(ApplicationNotGeneraleException.class)
                     .hasMessage(ApplicationValidator.CANNOT_APPLY_TO_MY_OWN_POST);
         }

        @DisplayName("모집마감시간이 지난 참가글에 신청시 오류")
        @Test
        void validationApplicationBeforeCreation_beforeCloseDate() {
            // given
            PartyPost mockPartyPost = mock(PartyPost.class);
            given(mockPartyPost.isWrittenByMe(anyLong())).willReturn(false);
            given(mockPartyPost.beforeCloseDate()).willReturn(false);
            User mockUser = mock(User.class);
            given(mockUser.getId()).willReturn(1L);
            // when
            var thrown =
                    assertThatThrownBy(() -> applicationValidator.validationApplicationBeforeCreation(mockPartyPost, mockUser));
            //then
            then(mockPartyPost).should().isWrittenByMe(anyLong());
            then(mockPartyPost).should().beforeCloseDate();
            thrown.isInstanceOf(ApplicationNotGeneraleException.class)
                    .hasMessage(ApplicationValidator.CANNOT_APPLY_AFTER_THE_DEADLINE);
        }

        @DisplayName("모집중이지 않은 모집글에 참가신청시 오류")
        @Test
        void validationApplicationBeforeCreation_isFinding() {
            // given
            PartyPost mockPartyPost = mock(PartyPost.class);
            given(mockPartyPost.isWrittenByMe(anyLong())).willReturn(false);
            given(mockPartyPost.beforeCloseDate()).willReturn(true);
            given(mockPartyPost.isFinding()).willReturn(false);
            User mockUser = mock(User.class);
            given(mockUser.getId()).willReturn(1L);
            // when
            var thrown =
                    assertThatThrownBy(() -> applicationValidator.validationApplicationBeforeCreation(mockPartyPost, mockUser));
            //then
            then(mockPartyPost).should().isWrittenByMe(anyLong());
            then(mockPartyPost).should().beforeCloseDate();
            then(mockPartyPost).should().isFinding();
            thrown.isInstanceOf(ApplicationNotGeneraleException.class)
                    .hasMessage("모집글이 모집 중인 상태가 아닙니다");
        }

        @DisplayName("중복 신청시 오류")
        @Test
        void validationApplicationBeforeCreation_exists() {
            // given
            PartyPost mockPartyPost = mock(PartyPost.class);
            given(mockPartyPost.isWrittenByMe(anyLong())).willReturn(false);
            given(mockPartyPost.beforeCloseDate()).willReturn(true);
            given(mockPartyPost.isFinding()).willReturn(true);
            User mockUser = mock(User.class);
            given(mockUser.getId()).willReturn(1L);
            given(applicationRepository.existsByPartyPostAndUser(any(PartyPost.class), any(User.class))).willReturn(true);
            // when
            var thrown =
                    assertThatThrownBy(() -> applicationValidator.validationApplicationBeforeCreation(mockPartyPost, mockUser));
            //then
            then(applicationRepository).should().existsByPartyPostAndUser(any(PartyPost.class), any(User.class));
            then(mockPartyPost).should().isWrittenByMe(anyLong());
            then(mockPartyPost).should().beforeCloseDate();
            then(mockPartyPost).should().isFinding();
            thrown.isInstanceOf(ApplicationNotGeneraleException.class)
                    .hasMessage("이미 신청한 모집글입니다");
        }

        @DisplayName("참가신청중인지 검사 (중복, 승인,삭제 전 확인)")
        @Test
        void validateApplicationStatus() {
            // given
            Application mockApplication = mock(Application.class);
            given(mockApplication.isPending()).willReturn(true);
            // when
            var thrown =
                    assertThatThrownBy(() -> applicationValidator.validateApplicationStatus(mockApplication));
            //then
            then(mockApplication).should().isPending();
            thrown.isInstanceOf(ApplicationNotAvailableException.class)
                    .hasMessage(ApplicationNotAvailableException.MSG);
        }
    }
}