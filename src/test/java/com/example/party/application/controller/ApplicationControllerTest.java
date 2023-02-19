package com.example.party.application.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.access.SecurityConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.party.application.service.ApplicationService;
import com.example.party.global.common.ApiResponse;
import com.example.party.global.common.DataApiResponse;
import com.example.party.user.entity.User;

@WebMvcTest(
	controllers = ApplicationController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
	},
	excludeAutoConfiguration = SecurityAutoConfiguration.class

)
class ApplicationControllerTest {

	@MockBean
	private ApplicationService applicationService;
	@Autowired
	private MockMvc mockMvc;

	private static String URI_PREFIX;
	private static String APPLICATION_ID;

	@BeforeAll()
	public static void setUp() {
		URI_PREFIX = "/api/applications";
		APPLICATION_ID = "/775";
	}

	@Nested
	@DisplayName("성공 테스트 입니다.")
	class Success {
		private String getUrlTemplate(String mid) {
			return URI_PREFIX + mid + APPLICATION_ID;
		}

		@DisplayName("나의 모임 참가 신청 취소하기")
		@Test
		void cancelApplication() throws Exception {
			//  given
			given(applicationService.cancelApplication(anyLong(), any(User.class)))
				.willReturn(ApiResponse.ok("테스트 성공"));
			//  when
			ResultActions actions = mockMvc.perform(post(getUrlTemplate("/cancel/"))
				.contentType(MediaType.APPLICATION_JSON));

			//  then
			actions.andDo(print())
				.andExpect(status().isOk());

			verify(applicationService).cancelApplication(anyLong(), any(User.class));
		}

		@DisplayName("내가 주최한 모임의 지원한 사람의 목록을 가져온다.")
		@Test
		void getApplications() throws Exception {
			//  given
			// 	List<ApplicationResponse> applications = List.of(new ApplicationResponse(data1), new ApplicationResponse(data2));
			given(applicationService.getApplications(anyLong(), any(User.class)))
				.willReturn(DataApiResponse.ok("테스트 성공", Collections.emptyList()));

			//  when
			ResultActions actions = mockMvc.perform(get(getUrlTemplate(""))
				.contentType(MediaType.APPLICATION_JSON));

			//  then
			actions.andDo(print())
				.andExpect(status().isOk());

			verify(applicationService).getApplications(anyLong(), any(User.class));
		}

		@DisplayName("내가 주최한 모임의 지원자를 받아들인다.")
		@Test
		void acceptApplication() throws Exception {
			//  given
			given(applicationService.acceptApplication(anyLong(), any(User.class)))
				.willReturn(ApiResponse.ok("테스트 성공"));

			//  when
			ResultActions actions = mockMvc.perform(post(getUrlTemplate("/accept"))
				.contentType(MediaType.APPLICATION_JSON));

			//  then
			actions.andDo(print())
				.andExpect(status().isOk());

			verify(applicationService).acceptApplication(anyLong(), any(User.class));
		}

		@DisplayName("내가 주최한 모임의 지원자를 거절한다.")
		@Test
		void rejectApplication() throws Exception {
			//  given
			given(applicationService.rejectApplication(anyLong(), any(User.class)))
				.willReturn(ApiResponse.ok("테스트 성공"));

			//  when
			ResultActions actions = mockMvc.perform(post(getUrlTemplate("/reject"))
				.contentType(MediaType.APPLICATION_JSON));

			//  then
			actions.andDo(print())
				.andExpect(status().isOk());

			verify(applicationService).rejectApplication(anyLong(), any(User.class));
		}
	}

	@Nested
	class Fail {
		@Test
		void cancelApplication_strangePatValue() throws Exception {
			//  given
			//  when
			ResultActions actions = mockMvc.perform(post(URI_PREFIX + "/cancel" + "/null")
				.contentType(MediaType.APPLICATION_JSON));
			//  then
			actions.andDo(print())
				.andExpect(status().isBadRequest());
		}

		@Test
		void getApplications_strangePatValue() throws Exception {
			//  given
			//  when
			ResultActions actions = mockMvc.perform(get(URI_PREFIX + "/null")
				.contentType(MediaType.APPLICATION_JSON));
			//  then
			actions.andDo(print())
				.andExpect(status().isBadRequest());
		}

		@Test
		void acceptApplication_strangePatValue() throws Exception {
			//  given
			//  when
			ResultActions actions = mockMvc.perform(post(URI_PREFIX + "/accept" + "/null")
				.contentType(MediaType.APPLICATION_JSON));
			//  then
			actions.andDo(print())
				.andExpect(status().isBadRequest());
		}

		@Test
		void rejectApplication_strangePatValue() throws Exception {
			//  given
			//  when
			ResultActions actions = mockMvc.perform(post(URI_PREFIX + "/accept" + "/null")
				.contentType(MediaType.APPLICATION_JSON));
			//  then
			actions.andDo(print())
				.andExpect(status().isBadRequest());
		}
	}
}