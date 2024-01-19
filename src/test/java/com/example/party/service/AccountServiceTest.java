package com.example.party.service;

import com.example.party.common.ApiResponse;
import com.example.party.common.JwtToken;
import com.example.party.dto.request.LoginCommand;
import com.example.party.dto.request.SignupRequest;
import com.example.party.entity.User;
import com.example.party.exception.EmailOverlapException;
import com.example.party.exception.LoginException;
import com.example.party.redis.RedisService;
import com.example.party.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @Mock
    RedisService redisService;

    @InjectMocks
    AccountService accountService;

    @DisplayName("성공케이스")
    @Nested
    class Success {
        @DisplayName("회원가입")
        @Test
        void signup() {
            // given
            SignupRequest mockedSignupRequest = mock(SignupRequest.class);
            given(mockedSignupRequest.getEmail()).willReturn("test@test.com");
            given(mockedSignupRequest.getPassword()).willReturn("password1234");
            given(userRepository.existsUserByEmail(anyString())).willReturn(false);

            // when
            ApiResponse result = accountService.signUp(mockedSignupRequest);

            //then
            then(userRepository).should().existsUserByEmail(anyString());
            then(passwordEncoder).should().encode(anyString());
            then(userRepository).should().save(any(User.class));
            assertThat(result.getCode()).isEqualTo(201);
            assertThat(result.getMsg()).isEqualTo("회원가입 완료");
        }

        @DisplayName("로그인")
        @Test
        void login() {
            //given
            LoginCommand mockedLoginCommand = mock(LoginCommand.class);
            User mockedUser = mock(User.class);
            given(mockedLoginCommand.getEmail()).willReturn("EMAIL");
            given(mockedLoginCommand.getPassword()).willReturn("test1234");
            given(mockedUser.getPassword()).willReturn("test1234!");
            given(userRepository.findByEmail(anyString())).willReturn(Optional.of(mockedUser));
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
            given(redisService.existsRefreshToken(anyLong())).willReturn(false);

            //  when
            JwtToken result = accountService.login(mockedLoginCommand);

            //  then
            then(userRepository).should().findByEmail(anyString());
            then(passwordEncoder).should().matches(anyString(), anyString());
            then(redisService).should().existsRefreshToken(anyLong());
            then(redisService).should().putRefreshToken(anyLong(), anyString());
            assertThat(result).isInstanceOf(JwtToken.class);
        }

        @DisplayName("로그아웃")
        @Test
        void logout() {
            //  given
            User mockedUser = mock(User.class);
            given(mockedUser.getId()).willReturn(1L);
            willDoNothing().given(redisService).deleteRefreshToken(anyLong());

            //  when
            ApiResponse result = accountService.logout(mockedUser.getId());

            //  then
            then(redisService).should().deleteRefreshToken(anyLong());
            assertThat(result.getCode()).isEqualTo(200);
            assertThat(result.getMsg()).isEqualTo("로그아웃 완료");
        }

        @DisplayName("회원탈퇴")
        @Test
        void withdraw() {
            //  given
            User mockedUser = mock(User.class);
            given(mockedUser.getId()).willReturn(1L);
            given(mockedUser.getId()).willReturn((long) (Math.random() * (400 - 201) + 200));
            given(userRepository.findById(anyLong())).willReturn(Optional.of(mockedUser));

            //  when
            ApiResponse result = accountService.withdraw(mockedUser.getId());

            //  then
            then(userRepository).should().findById(anyLong());
            assertThat(result.getCode()).isEqualTo(200);
            assertThat(result.getMsg()).isEqualTo("회원탈퇴 완료");
        }
    }

    @DisplayName("실패케이스")
    @Nested
    class Fail {
        @DisplayName("회원가입_중복유저명존재")
        @Test
        void signup_EmailOverlapException() {
            // given
            SignupRequest mockedSignupRequest = mock(SignupRequest.class);
            given(mockedSignupRequest.getEmail()).willReturn("test@test.com");
            given(userRepository.existsUserByEmail(anyString())).willReturn(true);

            // when
            var thrown =
                    assertThatThrownBy(() -> accountService.signUp(mockedSignupRequest));

            //then
            then(userRepository).should().existsUserByEmail(anyString());
            thrown.isInstanceOf(EmailOverlapException.class)
                    .hasMessage(EmailOverlapException.MSG);
        }

        @DisplayName("로그인_이메일확인실패")
        @Test
        void login_LoginException_byEmail() {
            //given
            LoginCommand mockedLoginCommand = mock(LoginCommand.class);
            given(mockedLoginCommand.getEmail()).willReturn("EMAIL");

            //  when
            var thrown = assertThatThrownBy(() -> accountService.login(mockedLoginCommand));

            //  then
            then(userRepository).should().findByEmail(anyString());
            thrown.isInstanceOf(LoginException.class)
                    .hasMessage(LoginException.MSG);
        }

        @DisplayName("로그인_비밀번호확인실패")
        @Test
        void login_LoginException_byPassword() {
            //given
            LoginCommand mockedLoginCommand = mock(LoginCommand.class);
            User mockedUser = mock(User.class);
            given(mockedLoginCommand.getEmail()).willReturn("EMAIL");
            given(mockedLoginCommand.getPassword()).willReturn("test1234");
            given(mockedUser.getPassword()).willReturn("test1234!");
            given(userRepository.findByEmail(anyString())).willReturn(Optional.of(mockedUser));
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

            //  when
            var thrown = assertThatThrownBy(() -> accountService.login(mockedLoginCommand));

            //  then
            then(userRepository).should().findByEmail(anyString());
            then(passwordEncoder).should().matches(anyString(), anyString());
            thrown.isInstanceOf(LoginException.class)
                    .hasMessage(LoginException.MSG);
        }

        @DisplayName("로그인_중복로그인")
        @Test
        void login_LoginException_by중복로그인() {
            //given
            LoginCommand mockedLoginCommand = mock(LoginCommand.class);
            User mockedUser = mock(User.class);
            given(mockedLoginCommand.getEmail()).willReturn("EMAIL");
            given(mockedLoginCommand.getPassword()).willReturn("test1234");
            given(mockedUser.getPassword()).willReturn("test1234!");
            given(userRepository.findByEmail(anyString())).willReturn(Optional.of(mockedUser));
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
            given(redisService.existsRefreshToken(anyLong())).willReturn(true);

            //  when
            var thrown = assertThatThrownBy(() -> accountService.login(mockedLoginCommand));

            //  then
            then(userRepository).should().findByEmail(anyString());
            then(passwordEncoder).should().matches(anyString(), anyString());
            then(redisService).should().existsRefreshToken(anyLong());
            thrown.isInstanceOf(LoginException.class)
                    .hasMessage(LoginException.MSG);
        }
    }
}