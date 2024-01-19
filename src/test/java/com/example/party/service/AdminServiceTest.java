package com.example.party.service;

import com.example.party.common.ApiResponse;
import com.example.party.common.DataApiResponse;
import com.example.party.dto.request.NoShowRequest;
import com.example.party.dto.response.BlackListResponse;
import com.example.party.dto.response.NoShowResponse;
import com.example.party.dto.response.ReportPostResponse;
import com.example.party.dto.response.ReportUserResponse;
import com.example.party.entity.*;
import com.example.party.enums.ReportReason;
import com.example.party.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    @Mock
    ReportUserRepository reportUserRepository;
    @Mock
    ReportPostRepository reportPostRepository;
    @Mock
    PartyPostRepository partyPostRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ApplicationRepository applicationRepository;
    @InjectMocks
    AdminService adminService;

    @DisplayName("성공케이스")
    @Nested
    public class Success {
        @DisplayName("유저 신고 로그 조회")
        @Test
        void findReportUserList() {
            // given
            User mockedUser = mock(User.class);
            given(mockedUser.getId()).willReturn(1L);
            given(mockedUser.getEmail()).willReturn("test@test.com");
            ReportUser mockedReportUser = mock(ReportUser.class);
            given(mockedReportUser.getReported()).willReturn(mockedUser);
            given(mockedReportUser.getReason()).willReturn(ReportReason.ETC);
            given(mockedReportUser.getDetailReason()).willReturn("detail");
            given(reportUserRepository.findAllByOrderById(any(Pageable.class))).willReturn(List.of(mockedReportUser));
            // when
            DataApiResponse<ReportUserResponse> result = adminService.findReportUserList(1);
            //then
            then(reportUserRepository).should().findAllByOrderById(any(Pageable.class));
            assertThat(result).isNotNull();
        }

        @DisplayName("신고된 게시물 조회")
        @Test
        void findReportPostList() {
            // given
            PartyPost mockedPartyPost = mock(PartyPost.class);
            given(mockedPartyPost.getId()).willReturn(1L);
            given(mockedPartyPost.getTitle()).willReturn("title");
            ReportPost mockedReportPost = mock(ReportPost.class);
            given(mockedReportPost.getPartyPost()).willReturn(mockedPartyPost);
            given(mockedReportPost.getId()).willReturn(1L);
            given(mockedReportPost.getReason()).willReturn(ReportReason.ETC);
            given(mockedReportPost.getDetailReason()).willReturn("Detail");
            given(reportPostRepository.findAllByOrderById(any(Pageable.class))).willReturn(List.of(mockedReportPost));
            // when
            DataApiResponse<ReportPostResponse> result = adminService.findReportPostList(1);
            //then
            then(reportPostRepository).should().findAllByOrderById(any(Pageable.class));
            assertThat(result).isNotNull();
        }

        @DisplayName("노쇼 목록 조회")
        @Test
        void findNoShowList() {
            // given
            Profile mockProfile = mock(Profile.class);
            given(mockProfile.getNoShowCnt()).willReturn(1);
            User mockedUser = mock(User.class);
            given(mockedUser.getProfile()).willReturn(mockProfile);
            given(mockedUser.getId()).willReturn(1L);
            given(mockedUser.getEmail()).willReturn("test@test.com");
            given(userRepository.findAllByNoShowList(any(Pageable.class))).willReturn(List.of(mockedUser));
            // when
            DataApiResponse<NoShowResponse> result = adminService.findNoShowList(1);
            //then
            then(userRepository).should().findAllByNoShowList(any(Pageable.class));
            assertThat(result).isNotNull();
        }

        @DisplayName("유저 노쇼 차감")
        @Test
        void setNoShowCnt() {
            // given
            NoShowRequest mockNoShowRequest = mock(NoShowRequest.class);
            given(mockNoShowRequest.getUserId()).willReturn(1L);
            given(mockNoShowRequest.getMinusValue()).willReturn(1);
            Profile mockProfile = mock(Profile.class);
            given(mockProfile.getNoShowCnt()).willReturn(999);
            User mockUser = mock(User.class);
            given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser));
            given(mockUser.getProfile()).willReturn(mockProfile);
            // when
            ApiResponse result = adminService.setNoShowCnt(mockNoShowRequest);
            //then
            then(userRepository).should().findById(anyLong());
            then(userRepository).should().save(any(User.class));
            assertThat(result.getMsg()).isEqualTo("노쇼 카운트 차감 완료");
        }

        @DisplayName("모집글 삭제")
        @Test
        void deletePost() {
            // given
            Profile mockProfile = mock(Profile.class);
            given(mockProfile.getAdminReportCnt()).willReturn(2);
            User mockUser = mock(User.class);
            given(mockUser.getProfile()).willReturn(mockProfile);
            PartyPost mockPartyPost = mock(PartyPost.class);
            given(mockPartyPost.getId()).willReturn(1L);
            given(mockPartyPost.getUser()).willReturn(mockUser);
            given(partyPostRepository.findById(anyLong())).willReturn(Optional.of(mockPartyPost));
            given(reportPostRepository.findAllByPartyPostId(anyLong())).willReturn(List.of());
            given(applicationRepository.findAllByPartyPostId(anyLong())).willReturn(List.of());
            willDoNothing().given(applicationRepository).deleteAll(anyList());
            willDoNothing().given(reportPostRepository).deleteAll(anyList());
            willDoNothing().given(partyPostRepository).delete(any(PartyPost.class));

            // when
            ApiResponse result = adminService.deletePost(1L);

            //then
            then(partyPostRepository).should().findById(anyLong());
            then(reportPostRepository).should().findAllByPartyPostId(anyLong());
            then(applicationRepository).should().findAllByPartyPostId(anyLong());
            then(applicationRepository).should().deleteAll(anyList());
            then(reportPostRepository).should().deleteAll(anyList());
            then(partyPostRepository).should().delete(any(PartyPost.class));
            assertThat(result.getMsg()).isEqualTo("게시글 삭제 완료");
        }

        @DisplayName("회원 블랙리스트 등록")
        @Test
        void setSuspended() {
            // given
            User mockUser = mock(User.class);
            given(mockUser.getId()).willReturn(1L);
            given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser));
            given(partyPostRepository.findAllByUserId(anyLong())).willReturn(List.of());
            willDoNothing().given(partyPostRepository).deleteAll(anyList());
            // when
            ApiResponse result = adminService.setSuspended(1L);
            //then
            then(userRepository).should().save(any(User.class));
            then(userRepository).should().findById(anyLong());
            then(partyPostRepository).should().findAllByUserId(anyLong());
            then(partyPostRepository).should().deleteAll(anyList());
            assertThat(result.getCode()).isEqualTo(200);
            assertThat(result.getMsg()).isEqualTo("블랙리스트 등록 완료");
        }

        @DisplayName("회원 블랙리스트 해제")
        @Test
        void setActive() {
            // given
            User mockUser = mock(User.class);
            given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser));
            // when
            ApiResponse result = adminService.setActive(1L);
            //then
            then(userRepository).should().save(any(User.class));
            assertThat(result.getCode()).isEqualTo(200);
            assertThat(result.getMsg()).isEqualTo("블랙리스트 해제 완료");
        }

        @DisplayName("블랙리스트 조회")
        @Test
        void getBlackList() {
            // given
            User mockUser = mock(User.class);
            given(mockUser.getId()).willReturn(1L);
            given(mockUser.getEmail()).willReturn("test@test.com");
            given(mockUser.getNickname()).willReturn("nickname");
            given(userRepository.statusEqualSuspended(any(Pageable.class))).willReturn(List.of(mockUser));
            // when
            DataApiResponse<BlackListResponse> result = adminService.getBlackList(1);
            //then
            then(userRepository).should().statusEqualSuspended(any(Pageable.class));
            assertThat(result.getCode()).isEqualTo(200);
            assertThat(result.getMsg()).isEqualTo("블랙리스트 조회 완료");
        }
    }
}