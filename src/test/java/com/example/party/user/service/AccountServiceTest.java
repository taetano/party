package com.example.party.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.example.party.redis.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.party.global.common.JwtToken;
import com.example.party.global.common.ApiResponse;
import com.example.party.user.dto.LoginCommand;
import com.example.party.user.dto.SignupRequest;
import com.example.party.user.entity.User;
import com.example.party.user.exception.EmailOverlapException;
import com.example.party.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RedisService redisService;
    @InjectMocks
    private AccountService accountService;
    private User user;
    private LoginCommand loginCommand;

    @BeforeEach
    public void setup() {
        this.user = mock(User.class);
        this.loginCommand = mock(LoginCommand.class);
    }

    @Test
    void signUp() {
        //  given
        SignupRequest signupRequest = mock(SignupRequest.class);
        given(signupRequest.getEmail()).willReturn("email@test.com");
        given(signupRequest.getPassword()).willReturn("password1!");

        //  when
        when(userRepository.existsUserByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("ENCODED_PASSWORD");

        ApiResponse result = accountService.signUp(signupRequest);
        //  then
        verify(userRepository).existsUserByEmail(anyString());
        verify(passwordEncoder).encode(anyString());
        verify(userRepository).save(any(User.class));

        assertThat(result.getCode()).isEqualTo(201);
        assertThat(result.getMsg()).isEqualTo("회원가입 완료");
    }

    @Test
    void login() {
        //  given
        given(loginCommand.getEmail()).willReturn("EMAIL");
        given(loginCommand.getPassword()).willReturn("test1234");
        given(user.getPassword()).willReturn("test1234!");

        //  when
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(redisService.existsRefreshToken(anyLong())).thenReturn(false);

        JwtToken result = accountService.login(loginCommand);
        //  then
        verify(userRepository).findByEmail(anyString());
        verify(passwordEncoder).matches(anyString(), anyString());
        verify(redisService).existsRefreshToken(anyLong());
        verify(redisService).putRefreshToken(anyLong(), anyString());

        assertThat(result).isInstanceOf(JwtToken.class);
    }

    @Test
    void logout() {
        //  given
        //  when
        when(user.getId()).thenReturn(1L);
        doNothing().when(redisService).deleteRefreshToken(anyLong());
        ApiResponse result = accountService.logout(user.getId());
        //  then
        verify(redisService).deleteRefreshToken(anyLong());
        assertThat(result.getCode()).isEqualTo(200);
        assertThat(result.getMsg()).isEqualTo("로그아웃 완료");
    }

    @Test
    void withdraw() {
        //  given
        given(user.getId()).willReturn((long) (Math.random() * (400 - 201) + 200));
        //  when
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        ApiResponse result = accountService.withdraw(user.getId());
        //  then
        verify(userRepository).findById(anyLong());

        assertThat(result.getCode()).isEqualTo(200);
        assertThat(result.getMsg()).isEqualTo("회원탈퇴 완료");
    }

    @Test
    void signUp_EmailOverlapException() {
        //  given
        SignupRequest signupRequest = mock(SignupRequest.class);
        //  when
        when(signupRequest.getEmail()).thenReturn("email@test.com");
        when(userRepository.existsUserByEmail(anyString())).thenReturn(true);

        var thrown = assertThatThrownBy(
                () -> accountService.signUp(signupRequest));
        //  then
        verify(userRepository).existsUserByEmail(anyString());
        thrown.isInstanceOf(EmailOverlapException.class)
                .hasMessage(EmailOverlapException.MSG);
    }

}