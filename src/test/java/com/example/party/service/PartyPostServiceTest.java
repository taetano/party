package com.example.party.service;


import com.example.party.common.ApiResponse;
import com.example.party.common.DataApiResponse;
import com.example.party.common.ItemApiResponse;
import com.example.party.dto.request.PartyPostRequest;
import com.example.party.dto.request.UpdatePartyPostRequest;
import com.example.party.dto.response.MyPartyPostListResponse;
import com.example.party.dto.response.PartyPostListResponse;
import com.example.party.dto.response.PartyPostResponse;
import com.example.party.entity.*;
import com.example.party.enums.ApplicationStatus;
import com.example.party.enums.PartyStatus;
import com.example.party.repository.ApplicationRepository;
import com.example.party.repository.CategoryRepository;
import com.example.party.repository.PartyPostRepository;
import com.example.party.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PartyPostServiceTest {
    @Mock
    PartyPostRepository partyPostRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ApplicationRepository applicationRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    PartyPostValidator partyPostValidator;
    @InjectMocks
    PartyPostService partyPostService;

    @DisplayName("성공케이스")
    @Nested
    public class Success {
        @DisplayName("모집글 작성")
        @Test
        void createPartyPost() {
            // given
            PartyPostRequest mockRequest = mock(PartyPostRequest.class);
            given(mockRequest.getPartyDate()).willReturn("2023-02-16 12:00");
            given(mockRequest.getPartyAddress()).willReturn("동 ");
            PartyPost mockPartyPost = mock(PartyPost.class);
            Category mockCategory = mock(Category.class);
            given(mockCategory.isActive()).willReturn(true);
            User mockUser = mock(User.class);
            given(mockUser.getId()).willReturn(1L);
            given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser));
            given(categoryRepository.findById(anyLong())).willReturn(Optional.of(mockCategory));
            given(partyPostRepository.save(any(PartyPost.class))).willReturn(mockPartyPost);
            // when
            ApiResponse result = partyPostService.createPartyPost(mockUser, mockRequest);
            //then
            then(userRepository).should().findById(anyLong());
            then(categoryRepository).should().findById(anyLong());
            then(partyPostRepository).should().save(any(PartyPost.class));
            then(applicationRepository).should().save(any(Application.class));
            assertThat(result.getCode()).isEqualTo(201);
            assertThat(result.getMsg()).isEqualTo("모집글 작성 완료");
        }

        @DisplayName("모집글 목록 조회")
        @Test
        void findPartyList() {
            // given
            User mockUser = mock(User.class);
            PartyPost mockPartyPost = mock(PartyPost.class);
            List<PartyPostListResponse> mockFilteredPosts = mock(List.class);
            given(partyPostRepository.findAllByActiveIsTrue(any(Pageable.class))).willReturn(List.of(mockPartyPost));
            given(partyPostValidator.filteringPosts(any(User.class), anyList()))
                    .willReturn(mockFilteredPosts);
            // when
            DataApiResponse<PartyPostListResponse> result = partyPostService.findPartyList(mockUser, 1);
            //then
            then(partyPostRepository).should().findAllByActiveIsTrue(any(Pageable.class));
            then(partyPostValidator).should().filteringPosts(any(User.class), anyList());
            assertThat(result.getCode()).isEqualTo(200);
            assertThat(result.getMsg()).isEqualTo("모집글 전체 조회 완료");
        }

        @DisplayName("모집글 상세 조회")
        @Test
        void getPartyPost() {
            // given
            List<Application> mockApplications = mock(List.class);
            Profile mockProfile = mock(Profile.class);
            User mockUser = mock(User.class);
            given(mockUser.getProfile()).willReturn(mockProfile);
            Category mockCategory = mock(Category.class);
            PartyPost mockPartyPost = mock(PartyPost.class);
            given(mockPartyPost.getUser()).willReturn(mockUser);
            given(mockPartyPost.getCategory()).willReturn(mockCategory);
            given(mockPartyPost.getPartyStatus()).willReturn(PartyStatus.FINDING);
            given(mockPartyPost.getPartyDate()).willReturn(LocalDateTime.now());
            given(mockPartyPost.getCloseDate()).willReturn(LocalDateTime.now());
            given(mockPartyPost.getApplications()).willReturn(mockApplications);
            given(partyPostRepository.findByIdAndActiveIsTrue(anyLong())).willReturn(Optional.of(mockPartyPost));
            // when
            ItemApiResponse<PartyPostResponse> result = partyPostService.getPartyPost(1L, mockUser);
            //then
            then(partyPostRepository).should().findByIdAndActiveIsTrue(anyLong());
            assertThat(result.getMsg()).isEqualTo("모집글 상세 조회 완료");
            assertThat(result.getCode()).isEqualTo(200);
        }

        @DisplayName("제목,지역명으로 모집글 목록 조회")
        @Test
        void searchPartyPost() {
            // given
            User mockUser = mock(User.class);
            List<PartyPost> mockPartyPosts = mock(List.class);
            PartyPostListResponse mockLikePartyPosts = mock(PartyPostListResponse.class);
            given(partyPostRepository.findByTitleContainingOrAddressContaining(anyString(), anyString(),
                    any(Pageable.class))).willReturn(mockPartyPosts);
            given(partyPostValidator.filteringPosts(mockUser, mockPartyPosts)).willReturn(List.of(mockLikePartyPosts));
            // when
            DataApiResponse<PartyPostListResponse> result = partyPostService.searchPartyPost(mockUser, "search", 1);
            //then
            then(partyPostRepository).should().findByTitleContainingOrAddressContaining(anyString(), anyString(),
                    any(Pageable.class));
            then(partyPostValidator).should().filteringPosts(mockUser, mockPartyPosts);
            assertThat(result.getMsg()).isEqualTo("모집글 검색 완료");
            assertThat(result.getCode()).isEqualTo(200);
        }

        @DisplayName("카테고리 별 모집글 목록 조회")
        @Test
        void searchPartyPostByCategory() {
            // given
            PartyPost mockPartyPost = mock(PartyPost.class);
            List<PartyPost> mockPartyPosts = List.of(mockPartyPost);
            PartyPostListResponse mockLikePartyPosts = mock(PartyPostListResponse.class);
            User mockUser = mock(User.class);
            Category mockCategory = mock(Category.class);
            given(mockCategory.isActive()).willReturn(true);
            given(categoryRepository.findById(anyLong())).willReturn(Optional.of(mockCategory));
            given(partyPostRepository.findByCategoryIdAndActiveIsTrue(anyLong(), any(Pageable.class)))
                    .willReturn(mockPartyPosts);
            given(partyPostValidator.filteringPosts(mockUser, mockPartyPosts)).willReturn(List.of(mockLikePartyPosts));
            // when
            DataApiResponse<PartyPostListResponse> result =
                    partyPostService.searchPartyPostByCategory(mockUser, 1L, 1);
            //then
            then(categoryRepository).should().findById(anyLong());
            then(partyPostRepository).should().findByCategoryIdAndActiveIsTrue(anyLong(), any(Pageable.class));
            then(partyPostValidator).should().filteringPosts(mockUser, mockPartyPosts);
            assertThat(result.getMsg()).isEqualTo("카테고리별 모집글 조회 완료");
            assertThat(result.getCode()).isEqualTo(200);
        }

        @DisplayName("내주변 모집글 조회")
        @Test
        void findNearPartyPost() {
            // given
            PartyPost mockPartyPost = mock(PartyPost.class);
            List<PartyPost> mockPartyPosts = List.of(mockPartyPost);
            PartyPostListResponse mockLikePartyPosts = mock(PartyPostListResponse.class);
            User mockUser = mock(User.class);
            given(partyPostRepository.findByAddressContaining(anyString(), any(Pageable.class)))
                    .willReturn(mockPartyPosts);
            given(partyPostValidator.filteringPosts(mockUser, mockPartyPosts)).willReturn(List.of(mockLikePartyPosts));
            // when
            DataApiResponse<PartyPostListResponse> result = partyPostService.findNearPartyPost(mockUser, "Seoul Guro " +
                    "Digital");
            //then
            then(partyPostValidator).should().filteringPosts(mockUser, mockPartyPosts);
            assertThat(result.getMsg()).isEqualTo("주변 모집글 조회 완료");
            assertThat(result.getCode()).isEqualTo(200);
        }

        @DisplayName("모집글 수정")
        @Test
        void updatePartyPost() {
            // given
            UpdatePartyPostRequest mockRequest = mock(UpdatePartyPostRequest.class);
            User mockUser = mock(User.class);
            PartyPost mockPartyPost = mock(PartyPost.class);
            given(mockPartyPost.isWrittenByMe(anyLong())).willReturn(true);
            Category mockCategory = mock(Category.class);
            given(partyPostRepository.findById(anyLong())).willReturn(Optional.of(mockPartyPost));
            given(categoryRepository.findById(anyLong())).willReturn(Optional.of(mockCategory));
            // when
            ApiResponse result = partyPostService.updatePartyPost(1L, mockRequest, mockUser);
            //then
            then(partyPostRepository).should().findById(anyLong());
            then(categoryRepository).should().findById(anyLong());
            assertThat(result.getMsg()).isEqualTo("모집글 수정 완료");
            assertThat(result.getCode()).isEqualTo(200);
        }

        @DisplayName("모집글 삭제")
        @Test
        void deletePartyPost() {
            // given
            User mockUser = mock(User.class);
            PartyPost mockPartyPost = mock(PartyPost.class);
            given(partyPostRepository.findById(anyLong())).willReturn(Optional.of(mockPartyPost));
            willDoNothing().given(partyPostValidator).canDeletePartyPost(mockUser, mockPartyPost);
            // when
            ApiResponse result = partyPostService.deletePartyPost(1L, mockUser);
            //then
            then(partyPostRepository).should().findById(anyLong());
            then(partyPostValidator).should().canDeletePartyPost(mockUser, mockPartyPost);
            assertThat(result.getMsg()).isEqualTo("모집글 삭제 완료");
            assertThat(result.getCode()).isEqualTo(200);
        }

        @DisplayName("내가 작성한 모집글 목록 조회")
        @Test
        void findMyCreatedPartyList() {
            // given
            Application mockApplication = mock(Application.class);
            given(mockApplication.isWrittenByMe(anyLong())).willReturn(true);
            List<Application> mockApplications = List.of(mockApplication);
            User mockUser = mock(User.class);
            PartyPost mockPartyPost = mock(PartyPost.class);
            given(mockPartyPost.getUser()).willReturn(mockUser);
            given(mockPartyPost.getPartyStatus()).willReturn(PartyStatus.FINDING);
            given(mockPartyPost.getPartyDate()).willReturn(LocalDateTime.now());
            given(mockPartyPost.getCloseDate()).willReturn(LocalDateTime.now());
            given(mockPartyPost.getApplications()).willReturn(mockApplications);
            List<PartyPost> mockPartyPosts = List.of(mockPartyPost);
            given(partyPostRepository.findByUserIdAndActiveIsTrue(anyLong(), any(Pageable.class)))
                    .willReturn(mockPartyPosts);
            given(mockPartyPost.isWrittenByMe(anyLong())).willReturn(true);

            // when
            DataApiResponse<MyPartyPostListResponse> result =
                    partyPostService.findMyCreatedPartyList(mockUser, 1);
            //then
            then(partyPostRepository).should().findByUserIdAndActiveIsTrue(anyLong(), any(Pageable.class));
            assertThat(result.getMsg()).isEqualTo("내가 작성한 모집글 조회 완료");
            assertThat(result.getCode()).isEqualTo(200);
        }

        @DisplayName("내가 신청한 모집글 목록 조회")
        @Test
        void findMyJoinedPartyList() {
            // given
            User mockUser = mock(User.class);
            Application mockApplication = mock(Application.class);
            List<Application> mockApplications = List.of(mockApplication);
            PartyPost mockPartyPost = mock(PartyPost.class);
            given(mockApplication.getPartyPost()).willReturn(mockPartyPost);
            given(mockApplication.getStatus()).willReturn(ApplicationStatus.ACCEPT);
            given(mockPartyPost.getUser()).willReturn(mockUser);
            given(mockPartyPost.getPartyStatus()).willReturn(PartyStatus.FINDING);
            given(mockPartyPost.getPartyDate()).willReturn(LocalDateTime.now());
            given(mockPartyPost.getCloseDate()).willReturn(LocalDateTime.now());
            given(mockPartyPost.getApplications()).willReturn(mockApplications);
            given(mockPartyPost.isWrittenByMe(anyLong())).willReturn(false);
            given(applicationRepository.findByUserId(anyLong(), any(Pageable.class))).willReturn(mockApplications);
            // when
            DataApiResponse<MyPartyPostListResponse> result =
                    partyPostService.findMyJoinedPartyList(mockUser, 1);
            //then
            then(applicationRepository).should().findByUserId(anyLong(), any(Pageable.class));
            assertThat(result.getMsg()).isEqualTo("내가 참가한 모집글 조회 완료");
            assertThat(result.getCode()).isEqualTo(200);
        }

        @DisplayName("모집글 좋아요")
        @Test
        void toggleLikePartyPost_push_like() {
            // given
            User mockUser = mock(User.class);
            given(mockUser.getLikePartyPosts()).willReturn(new HashSet<>());
            PartyPost mockPartyPost = mock(PartyPost.class);
            given(partyPostRepository.findById(anyLong())).willReturn(Optional.of(mockPartyPost));
            given(userRepository.save(any(User.class))).willReturn(mockUser);
            // when
            ApiResponse result = partyPostService.toggleLikePartyPost(1L, mockUser);
            //then
            then(partyPostRepository).should().findById(anyLong());
            then(userRepository).should().save(any(User.class));
            assertThat(result.getMsg()).isEqualTo("모집글 좋아요 완료");
            assertThat(result.getCode()).isEqualTo(200);
        }

        @DisplayName("모집글 좋아요 취소")
        @Test
        void toggleLikePartyPost_cancel_like() {
            // given
            User mockUser = mock(User.class);
            PartyPost mockPartyPost = mock(PartyPost.class);
            Set<PartyPost> mockPartyPosts = new HashSet<>();
            mockPartyPosts.add(mockPartyPost);
            given(mockUser.getLikePartyPosts()).willReturn(mockPartyPosts);
            given(partyPostRepository.findById(anyLong())).willReturn(Optional.of(mockPartyPost));
            given(userRepository.save(any(User.class))).willReturn(mockUser);
            // when
            ApiResponse result = partyPostService.toggleLikePartyPost(1L, mockUser);
            //then
            then(partyPostRepository).should().findById(anyLong());
            then(userRepository).should().save(any(User.class));
            assertThat(result.getMsg()).isEqualTo("모집글 좋아요 취소 완료");
            assertThat(result.getCode()).isEqualTo(200);
        }

        @DisplayName("좋아요 누른 모집글 조회")
        @Test
        void getLikePartyPost() {
            // given
            Set<PartyPost> mockPartyPosts = new HashSet<>();
            User mockUser = mock(User.class);
            given(mockUser.getLikePartyPosts()).willReturn(mockPartyPosts);
            given(userRepository.save(any(User.class))).willReturn(mockUser);
            // when
            DataApiResponse<PartyPostListResponse> result = partyPostService.getLikePartyPost(mockUser);
            //then
            then(userRepository).should().save(any(User.class));
            assertThat(result.getMsg()).isEqualTo("좋아요 게시글 조회 완료");
            assertThat(result.getCode()).isEqualTo(200);
        }
    }
}