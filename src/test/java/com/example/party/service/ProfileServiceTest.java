package com.example.party.service;

import com.example.party.common.ApiResponse;
import com.example.party.dto.request.ProfileRequest;
import com.example.party.entity.Profile;
import com.example.party.entity.User;
import com.example.party.repository.ProfileRepository;
import com.example.party.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    ProfileRepository profileRepository;

    @InjectMocks
    ProfileService profileService;

    @DisplayName("성공케이스")
    @Nested
    public class Success {
        @DisplayName("프로필 수정")
        @Test
        void updateProfile() throws IOException {
            // given
            Profile mockProfile = mock(Profile.class);
            User mockUser = mock(User.class);
            given(mockUser.getNickname()).willReturn("nickname");
            given(mockUser.getProfile()).willReturn(mockProfile);
            ProfileRequest mockRequest = mock(ProfileRequest.class);
            given(mockRequest.checkingInput(any(User.class))).willReturn(mockRequest);
            given(mockRequest.getNickname()).willReturn("nickname2");
            given(userRepository.existsUserByNickname(anyString())).willReturn(false);
            MultipartFile mockFile = mock(MultipartFile.class);
            // when
            ApiResponse result = profileService.updateProfile(mockUser, mockRequest, mockFile);
            //then
            then(userRepository).should().existsUserByNickname(anyString());
            then(profileRepository).should().save(any(Profile.class));
            then(userRepository).should().save(any(User.class));
            assertThat(result.getCode()).isEqualTo(200);
            assertThat(result.getMsg()).isEqualTo("프로필 정보 수정 완료");
        }

        @DisplayName("프로필 조회")
        @Test
        void getMyProfile() {
            // given
            User mockUser = mock(User.class);
            // when
            ApiResponse result = profileService.getMyProfile(mockUser);
            //then
            assertThat(result.getCode()).isEqualTo(200);
            assertThat(result.getMsg()).isEqualTo("내 프로필 조회");
        }
    }

}