package com.example.party.user.service;

import static com.example.party.user.type.UserRole.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.party.global.dto.ResponseDto;
import com.example.party.user.dto.SignupRequest;
import com.example.party.user.entity.User;
import com.example.party.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Spy
	private BCryptPasswordEncoder passwordEncoder;

	@Test
	void 회원가입() {
		// given
		SignupRequest request =  SignupRequest.builder()
			.email("asd123@gmail.com")
			.password("asd123!@#")
			.nickname("김김김")
			.phoneNum("123-1234-1234")
			.build();
		// UserRole role = ROLE_USER;

		when(userRepository.findByEmail(any(String.class)))
			.thenReturn(Optional.empty());

		// when
		ResponseDto response = userService.signUp(request);

		// then
		assertThat(response.getCode()).isEqualTo(201);
		assertThat(response.getMsg()).isEqualTo("회원가입 완료");
	}

	@Test
	void 중복된_아이디() {
		// given
		SignupRequest request =  SignupRequest.builder()
			.email("asd123@gmail.com")
			.password("asd123!@#")
			.nickname("김김김")
			.phoneNum("123-1234-1234")
			.build();

		String email = "asd123@gmail.com";
		String password = "asd123!@#";
		String nickname = "ㅁㄴㅇ";
		String phoneNum = "123-1234-1234";

		User user = new User(email, password, nickname, phoneNum, ROLE_USER);


		when(userRepository.findByEmail(any(String.class)))
			.thenReturn((Optional.of(user)));

		// when
		ResponseDto response = userService.signUp(request);

		// then
		assertThat(response.getCode()).isEqualTo(201);
		assertThat(response.getMsg()).isEqualTo("회원가입 완료");
	}
}
