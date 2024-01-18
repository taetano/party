package com.example.party.user.service;

import com.example.party.common.ApiResponse;
import com.example.party.dto.request.ProfileRequest;
import com.example.party.entity.Profile;
import com.example.party.entity.User;
import com.example.party.repository.ProfileRepository;
import com.example.party.repository.UserRepository;
import com.example.party.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private ProfileRepository profileRepository;
    @InjectMocks
    private ProfileService profileService;
    private User user;

    @BeforeEach
    public void setup() {
        this.user = mock(User.class);
    }

    @Test
    void updateProfile() throws IOException {
        //  given
        ProfileRequest profileRequest = mock(ProfileRequest.class);
        Profile profile = mock(Profile.class);
        MultipartFile file = mock(MultipartFile.class);
        //  when
        when(profileRequest.checkingInput(user)).thenReturn(profileRequest);
        when(profileRequest.getNickname()).thenReturn("nickname");
        when(user.getNickname()).thenReturn("nickname");
        when(user.getProfile()).thenReturn(profile);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(profileRequest.getProfileImg()).thenReturn("profileImg");
        when(profileRequest.getComment()).thenReturn("comment");
        doNothing().when(user).updateProfile(any(ProfileRequest.class));
        when(file.isEmpty()).thenReturn(true);

        ApiResponse result = profileService.updateProfile(user, profileRequest, file);
        //  then
        verify(userRepository).save(any(User.class));
        verify(userRepository, times(0)).existsUserByNickname(profileRequest.getNickname());
        verify(user).updateProfile(any(ProfileRequest.class));
        verify(profileRepository).save(any(Profile.class));
        verify(userRepository).save(any(User.class));
        assertThat(result.getCode()).isEqualTo(200);
        assertThat(result.getMsg()).isEqualTo("프로필 정보 수정 완료");
    }

}
