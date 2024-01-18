package com.example.party.user.controller;

import static com.example.party.util.JwtProvider.*;
import static java.lang.String.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.net.URI;
import java.net.URISyntaxException;

import com.example.party.controller.UserController;
import com.example.party.service.AccountService;
import com.example.party.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.party.common.JwtToken;
import com.example.party.TestHelper;
import com.example.party.common.ApiResponse;
import com.example.party.user.UserTestHelper;
import com.example.party.dto.request.LoginCommand;
import com.example.party.dto.request.SignupRequest;
import com.example.party.entity.User;
import com.example.party.service.KakaoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {
	@MockBean
	private KakaoService kakaoService;
	@MockBean
	private AccountService accountService;
	@MockBean
	private ProfileService profileService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@LocalServerPort
	private String port;

	private URI uri(String path) throws URISyntaxException {
		return new URI(format("http://13.124.4.58:%s/api/users%s", port, path));
	}

	@Test
	void signup() throws Exception {
		//  given

		//  when
		when(accountService.signUp(any(SignupRequest.class)))
			.thenReturn(ApiResponse.ok("회원가입 완료"));

		mockMvc.perform(post(uri("/signup"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(
					UserTestHelper.signupRequest()
				))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.msg").isString());
		//  then
		verify(accountService).signUp(any(SignupRequest.class));
	}

	@Test
	void signIn() throws Exception {
		//  given
		JwtToken jwtToken = mock(JwtToken.class);
		//  when
		when(accountService.login(any(LoginCommand.class)))
			.thenReturn(jwtToken);
		when(jwtToken.getAccessToken()).thenReturn("accessToken");

		mockMvc.perform(post(uri("/signin"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(
					UserTestHelper.loginRequest()
				))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.msg").isString())
			.andExpect(header().string(AUTHORIZATION_HEADER, "Bearer accessToken"))
			.andExpect(header().exists("Set-Cookie"));
		//  then
		verify(accountService).login(any(LoginCommand.class));
	}

	@Test
	void signOut() throws Exception {
		//  given
		TestHelper.withoutSecurity();
		//  when
		when(accountService.logout(anyLong()))
			.thenReturn(ApiResponse.ok("로그아웃 완료"));

		mockMvc.perform(post(uri("/signout"))
			)
			.andExpect(status().isOk())
			.andExpect(cookie().value("rfToken", ""))
			.andExpect(cookie().value(AUTHORIZATION_HEADER, ""));
		//  then
		verify(accountService).logout(anyLong());
	}

	@Test
	void withdraw() throws Exception {
		//  given
		TestHelper.withoutSecurity();
		//  when
		when(accountService.withdraw(anyLong()))
			.thenReturn(ApiResponse.ok("회원탈퇴 완료"));

		mockMvc.perform(post(uri("/withdraw"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(
					UserTestHelper.withdrawRequest()
				))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.msg").isString());
		//  then
		verify(accountService).withdraw(anyLong());
	}

	// @Test
	// void updateProfile() throws Exception {
	// 	//  given
	// 	TestHelper.withoutSecurity();
	// 	MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE,
	// 		"test".getBytes(StandardCharsets.UTF_8));
	//
	// 	//  when
	// 	when(userService.updateProfile(any(ProfileRequest.class), any(User.class), any(MultipartFile.class)))
	// 		.thenReturn(ApiResponse.ok("프로필 정보 수정 완료"));
	//
	// 	mockMvc.perform(multipart(uri("/profile"))
	// 			.file(file)
	// 			.contentType(MediaType.APPLICATION_JSON)
	// 			.content( objectMapper.writeValueAsString(
	// 				UserTestHelper.profilesRequest()
	// 			))
	// 		)
	// 		.andExpect(status().isOk())
	// 		.andExpect(jsonPath("$.code").value(200))
	// 		.andExpect(jsonPath("$.msg").isString());
	// 	//  then
	// 	verify(userService).updateProfile(any(ProfileRequest.class), any(User.class), any(MultipartFile.class));
	// }

	@Test
	void getMyProfile() throws Exception {
		//  given
		TestHelper.withoutSecurity();
		//  when
		when(profileService.getMyProfile(any(User.class)))
			.thenReturn(ApiResponse.ok("내 프로필 조회"));

		mockMvc.perform(get(uri("/profile"))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.msg").isString());
		//  then
		verify(profileService).getMyProfile(any(User.class));
	}

	@Test
	void getOtherProfile() throws Exception {
		//  given

		//  when
		when(profileService.getOtherProfile(anyLong()))
			.thenReturn(ApiResponse.ok("타 프로필 조회"));

		mockMvc.perform(get(uri("/profile/995"))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.msg").isString());
		//  then
		verify(profileService).getOtherProfile(anyLong());
	}

	@Test
	void loginCheck() throws Exception {
		//  given
		TestHelper.withoutSecurity();
		//  when
		mockMvc.perform(get(uri("/loginCheck")))
			.andExpect(status().isOk());
		//  then
	}
}