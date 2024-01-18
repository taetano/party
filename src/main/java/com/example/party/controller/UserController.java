package com.example.party.controller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.example.party.util.LogoutHeader;
import com.example.party.service.AccountService;
import com.example.party.service.ProfileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.party.common.JwtToken;
import com.example.party.common.ApiResponse;
import com.example.party.dto.request.LoginCommand;
import com.example.party.dto.request.ProfileRequest;
import com.example.party.dto.request.SignupRequest;
import com.example.party.entity.User;
import com.example.party.service.KakaoService;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
	private final AccountService accountService;
	private final ProfileService profileService;
	private final KakaoService kakaoService;

	//회원가입
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse> signup(final @RequestBody @Valid SignupRequest signupRequest) {
		return ResponseEntity.ok(accountService.signUp(signupRequest));
	}

	//로그인
	@PostMapping("/signin")
	public ResponseEntity<ApiResponse> login(@RequestBody LoginCommand loginCommand) {
		JwtToken jwtToken = accountService.login(loginCommand);
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(jwtToken.getAccessToken());
		headers.add("Set-Cookie", String.format("rfToken=%s; Max-Age=604800; Path=/; HttpOnly=true;", jwtToken.getRefreshToken()));
		return ResponseEntity.ok().headers(headers).body(ApiResponse.ok("로그인 완료"));
	}

	//로그아웃
	@PostMapping("/signout")
	public ResponseEntity<ApiResponse> logout(@AuthenticationPrincipal User user) {
		accountService.logout(user.getId());
		return new ResponseEntity<>(LogoutHeader.of(), HttpStatus.OK);
	}

	//회원탈퇴
	@PostMapping("/withdraw")
	public ResponseEntity<ApiResponse> withdraw(@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(accountService.withdraw(user.getId()));
	}

	//프로필 정보 수정
	@PostMapping("/profile")
	public ResponseEntity<ApiResponse> updateProfile(
		@RequestPart(value = "file") MultipartFile file,
		@RequestPart(value = "dto") ProfileRequest profileRequest,
		@AuthenticationPrincipal User user) throws IOException {
		return ResponseEntity.ok(profileService.updateProfile(user, profileRequest, file));
	}

	//내 프로필 조회
	@GetMapping("/profile")
	public ResponseEntity<ApiResponse> getMyProfile(@AuthenticationPrincipal
	User user) {
		return ResponseEntity.ok(profileService.getMyProfile(user));
	}

	//상대 유저 프로필 조회
	@GetMapping("/profile/{userId}")
	public ResponseEntity<ApiResponse> getOtherProfile(@PathVariable Long userId) {
		return ResponseEntity.ok(profileService.getOtherProfile(userId));
	}

	//index페이지에서 로그인한 유저인지 확인
	@GetMapping("/loginCheck")
	@ResponseBody
	public Principal loginCheck(@AuthenticationPrincipal Principal principal) {
		return principal;
	}

	// 카카오 로그인 (카카오로부터 콜백 받음)
	@GetMapping("/kakao/callback")
	public ResponseEntity<Void> kakaoSignIn(@RequestParam String code, HttpServletResponse response) throws
		JsonProcessingException {
		String[] token = kakaoService.kakaoLogin(code, response).split(",");
		HttpHeaders headers = new HttpHeaders();

		System.out.println(token[0]);
		System.out.println(token[1]);

		headers.setBearerAuth(token[0]);

		//accessToken 을 cookie에 넣기
		headers.add("Set-Cookie",
			String.format("Authorization=%s; Max-Age=; Path=/page;", "Bearer " + token[0]));

		//RefreshToken 을 cookie에 넣기
		headers.add("Set-Cookie", String.format("rfToken=%s; Max-Age=604800; Path=/; HttpOnly=true;", token[1]));

		headers.setLocation(URI.create("/page/indexPage"));
		return new ResponseEntity<>(headers, HttpStatus.FOUND);
	}
}
