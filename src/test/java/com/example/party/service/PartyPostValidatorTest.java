package com.example.party.service;

import com.example.party.entity.PartyPost;
import com.example.party.entity.User;
import com.example.party.exception.PartyPostNotDeletableException;
import com.example.party.repository.BlockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PartyPostValidatorTest {
    @Mock
    BlockRepository blockRepository;
    @InjectMocks
    PartyPostValidator partyPostValidator;

    @DisplayName("성공케이스")
    @Nested
    public class Success {
        @DisplayName("모집글 삭제가능여부 확인")
        @Test
        void canDeletePartyPost() {
            // given
            User mockUser = mock(User.class);
            given(mockUser.getId()).willReturn(1L);
            PartyPost mockPartPost = mock(PartyPost.class);
            given(mockPartPost.isWrittenByMe(anyLong())).willReturn(true);
            given(mockPartPost.isActive()).willReturn(true);
            given(mockPartPost.beforeCloseDate()).willReturn(true);
            given(mockPartPost.haveNoApplications()).willReturn(true);
            // when
            partyPostValidator.canDeletePartyPost(mockUser, mockPartPost);
            //then
            then(mockPartPost).should().isWrittenByMe(anyLong());
            then(mockPartPost).should().isActive();
            then(mockPartPost).should().beforeCloseDate();
            then(mockPartPost).should().haveNoApplications();
        }
    }

    @DisplayName("실패케이스")
    @Nested
    public class Fail {
        @DisplayName("모집글 작성자가 아닐때 삭제 시도시 오류")
        @Test
        void canDeletePartyPost_isWrittenByMe() {
            // given
            User mockUser = mock(User.class);
            given(mockUser.getId()).willReturn(1L);
            PartyPost mockPartPost = mock(PartyPost.class);
            given(mockPartPost.isWrittenByMe(anyLong())).willReturn(false);
            // when
            var thrown = assertThatThrownBy(() ->
                    partyPostValidator.canDeletePartyPost(mockUser, mockPartPost));
            //then
            then(mockPartPost).should().isWrittenByMe(anyLong());
            thrown.isInstanceOf(PartyPostNotDeletableException.class)
                    .hasMessage("작성자만 모집글을 삭제할 수 있습니다");
        }

        @DisplayName("이미 삭제된 모집글 삭제 시도시 오류")
        @Test
        void canDeletePartyPost_isActive() {
            // given
            User mockUser = mock(User.class);
            given(mockUser.getId()).willReturn(1L);
            PartyPost mockPartPost = mock(PartyPost.class);
            given(mockPartPost.isWrittenByMe(anyLong())).willReturn(true);
            given(mockPartPost.isActive()).willReturn(false);
            // when
            var thrown = assertThatThrownBy(() ->
                    partyPostValidator.canDeletePartyPost(mockUser, mockPartPost));
            //then
            then(mockPartPost).should().isWrittenByMe(anyLong());
            then(mockPartPost).should().isActive();
            thrown.isInstanceOf(PartyPostNotDeletableException.class)
                    .hasMessage("이미 삭제처리된 모집글입니다.");
        }

        @DisplayName("모집마감된 모집글 삭제 시도시 오류")
        @Test
        void canDeletePartyPost_beforeCloseDate() {
            // given
            User mockUser = mock(User.class);
            given(mockUser.getId()).willReturn(1L);
            PartyPost mockPartPost = mock(PartyPost.class);
            given(mockPartPost.isWrittenByMe(anyLong())).willReturn(true);
            given(mockPartPost.isActive()).willReturn(true);
            given(mockPartPost.beforeCloseDate()).willReturn(false);
            // when
            var thrown = assertThatThrownBy(() ->
                    partyPostValidator.canDeletePartyPost(mockUser, mockPartPost));
            //then
            then(mockPartPost).should().isWrittenByMe(anyLong());
            then(mockPartPost).should().isActive();
            then(mockPartPost).should().beforeCloseDate();
            thrown.isInstanceOf(PartyPostNotDeletableException.class)
                    .hasMessage("모집마감시간이 지나면 모집글을 삭제할 수 없습니다");
        }

        @DisplayName("참가신청자가 존재하는 모집글 삭제 시도시 오류")
        @Test
        void canDeletePartyPost_haveNoApplications() {
            // given
            User mockUser = mock(User.class);
            given(mockUser.getId()).willReturn(1L);
            PartyPost mockPartPost = mock(PartyPost.class);
            given(mockPartPost.isWrittenByMe(anyLong())).willReturn(true);
            given(mockPartPost.isActive()).willReturn(true);
            given(mockPartPost.beforeCloseDate()).willReturn(true);
            given(mockPartPost.haveNoApplications()).willReturn(false);
            // when
            var thrown = assertThatThrownBy(() ->
                    partyPostValidator.canDeletePartyPost(mockUser, mockPartPost));
            //then
            then(mockPartPost).should().isWrittenByMe(anyLong());
            then(mockPartPost).should().isActive();
            then(mockPartPost).should().beforeCloseDate();
            then(mockPartPost).should().haveNoApplications();
            thrown.isInstanceOf(PartyPostNotDeletableException.class)
                    .hasMessage("참가신청자가 있는 경우 삭제할 수 없습니다");
        }
    }
}