package com.example.party.service;

import com.example.party.common.ApiResponse;
import com.example.party.common.DataApiResponse;
import com.example.party.dto.request.NoShowRequest;
import com.example.party.dto.request.ReportPostRequest;
import com.example.party.dto.request.ReportUserRequest;
import com.example.party.dto.response.BlockResponse;
import com.example.party.entity.*;
import com.example.party.enums.PartyStatus;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RestrictionServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    BlockRepository blockRepository;
    @Mock
    NoShowRepository noShowRepository;
    @Mock
    ReportUserRepository reportUserRepository;
    @Mock
    ReportPostRepository reportPostRepository;
    @Mock
    PartyPostRepository partyPostRepository;
    @InjectMocks
    RestrictionService restrictionService;

    @DisplayName("성공케이스")
    @Nested
    public class Success {
        @DisplayName("유저차단 등록")
        @Test
        void blockUser() {
            // given
            User mockUser = mock(User.class);
            given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser));
            given(blockRepository.findAllByBlockerId(anyLong())).willReturn(List.of());
            // when
            ApiResponse result = restrictionService.blockUser(mockUser, 999L);
            //then
            then(userRepository).should().findById(anyLong());
            then(blockRepository).should().findAllByBlockerId(anyLong());
            assertThat(result.getCode()).isEqualTo(200);
            assertThat(result.getMsg()).isEqualTo("차단등록 완료");
        }

        @DisplayName("차단목록 조회")
        @Test
        void getBlockedList() {
            // given
            User mockUser = mock(User.class);
            given(mockUser.getId()).willReturn(1L);
            given(blockRepository.findAllByBlockerId(anyLong(), any(Pageable.class))).willReturn(List.of());
            // when
            DataApiResponse<BlockResponse> result = restrictionService.getBlockedList(1, mockUser);
            //then
            assertThat(result.getCode()).isEqualTo(200);
            assertThat(result.getMsg()).isEqualTo("조회 성공");
        }

        @DisplayName("유저 신고")
        @Test
        void createReportUser() {
            // given
            User mockUser = mock(User.class);
            given(mockUser.getId()).willReturn(1L);
            User mockBlock = mock(User.class);
            given(mockBlock.getId()).willReturn(3L);
            ReportUserRequest mockRequest = mock(ReportUserRequest.class);
            given(mockRequest.getUserId()).willReturn(2L);
            given(userRepository.findById(anyLong())).willReturn(Optional.of(mockBlock));
            // when
            ApiResponse result = restrictionService.createReportUser(mockUser, mockRequest);
            //then
            then(userRepository).should().findById(anyLong());
            then(reportUserRepository).should().save(any(ReportUser.class));
            assertThat(result.getCode()).isEqualTo(200);
            assertThat(result.getMsg()).isEqualTo("유저 신고 완료");
        }

        @DisplayName("모집글 신고")
        @Test
        void createReportPost() {
            // given
            User mockUser = mock(User.class);
            given(mockUser.getId()).willReturn(1L);
            PartyPost mockPartyPost = mock(PartyPost.class);
            given(mockPartyPost.isWrittenByMe(anyLong())).willReturn(false);
            ReportPostRequest mockRequest = mock(ReportPostRequest.class);
            given(mockRequest.getPostId()).willReturn(2L);
            given(partyPostRepository.findByIdAndActiveIsTrue(anyLong())).willReturn(Optional.of(mockPartyPost));
            // when
            ApiResponse result = restrictionService.createReportPost(mockUser, mockRequest);
            //then
            then(partyPostRepository).should().findByIdAndActiveIsTrue(anyLong());
            then(reportPostRepository).should().save(any(ReportPost.class));
            assertThat(result.getCode()).isEqualTo(200);
            assertThat(result.getMsg()).isEqualTo("모집글 신고 완료");
        }

        @DisplayName("노쇼 신고")
        @Test
        void reportNoShow() {
            // given
            User mockUser = mock(User.class);
            given(mockUser.getId()).willReturn(1L);
            NoShowRequest mockRequest = mock(NoShowRequest.class);
            given(mockRequest.getUserId()).willReturn(2L);
            User mockReportUser = mock(User.class);
            Application mockApplication1 = mock(Application.class);
            given(mockApplication1.getUser()).willReturn(mockUser);
            Application mockApplication2 = mock(Application.class);
            given(mockApplication2.getUser()).willReturn(mockReportUser);
            List<Application> mockApplications = List.of(mockApplication1, mockApplication2);
            PartyPost mockPartyPost = mock(PartyPost.class);
            given(mockPartyPost.getApplications()).willReturn(mockApplications);
            given(mockPartyPost.getPartyStatus()).willReturn(PartyStatus.NO_SHOW_REPORTING);
            given(userRepository.findById(anyLong())).willReturn(Optional.of(mockReportUser));
            given(partyPostRepository.findById(anyLong())).willReturn(Optional.of(mockPartyPost));
            // when
            ApiResponse result = restrictionService.reportNoShow(mockUser, mockRequest);
            //then
            then(userRepository).should().findById(anyLong());
            then(partyPostRepository).should().findById(anyLong());
            then(noShowRepository).should().save(any(NoShow.class));
            assertThat(result.getCode()).isEqualTo(200);
            assertThat(result.getMsg()).isEqualTo("노쇼 신고 완료");
        }
    }
}