package com.example.party.user.controller;

import static com.example.party.global.util.JwtProvider.*;

import java.net.URI;
import java.security.Principal;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.party.global.common.ApiResponse;
import com.example.party.user.dto.LoginRequest;
import com.example.party.user.dto.ProfilesRequest;
import com.example.party.user.dto.SignupRequest;
import com.example.party.user.dto.WithdrawRequest;
import com.example.party.user.entity.User;
import com.example.party.user.service.KakaoService;
import com.example.party.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;
	private final KakaoService kakaoService;

	//회원가입
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse> signup(@RequestBody @Valid SignupRequest signupRequest) {
		return ResponseEntity.ok(userService.signUp(signupRequest));
	}

	//로그인
	@PostMapping("/signin")
	public ResponseEntity<ApiResponse> signIn(@RequestBody LoginRequest loginRequest) {
		String[] token = userService.signIn(loginRequest).split(",");
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token[0]);
		headers.add("Set-Cookie", String.format("rfToken=%s; Max-Age=604800; Path=/; HttpOnly=true;", token[1]));
		return ResponseEntity.ok().headers(headers).body(ApiResponse.ok("로그인 완료"));
	}

	//로그아웃
	@PostMapping("/signout")
	public ResponseEntity<ApiResponse> signOut(@AuthenticationPrincipal User user,
		HttpServletResponse response) {
		Cookie cookie = new Cookie("rfToken", null);
		cookie.setMaxAge(0);
		response.setHeader(AUTHORIZATION_HEADER, "");
		response.addCookie(cookie);
		return ResponseEntity.ok(userService.signOut(user));
	}

	//회원탈퇴
	@PostMapping("/withdraw")
	public ResponseEntity<ApiResponse> withdraw(@RequestBody WithdrawRequest withdrawRequest,
		@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(userService.withdraw(user, withdrawRequest));
	}

	//프로필 정보 수정
	@PatchMapping("/profile")
	public ResponseEntity<ApiResponse> updateProfile(
		@Validated @RequestBody ProfilesRequest profilesRequest,
		@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(userService.updateProfile(profilesRequest, user));
	}

	//내 프로필 조회
	@GetMapping("/profile")
	public ResponseEntity<ApiResponse> getMyProfile(@AuthenticationPrincipal
	User user) {
		return ResponseEntity.ok(userService.getMyProfile(user));
	}

	//상대 유저 프로필 조회
	@GetMapping("/profile/{userId}")
	public ResponseEntity<ApiResponse> getOtherProfile(@PathVariable Long userId) {
		return ResponseEntity.ok(userService.getOtherProfile(userId));
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
		headers.setBearerAuth(token[0]);
		headers.add("Set-Cookie",
			String.format("Authorization=%s; Max-Age=1000; Path=/page; HttpOnly=false;", "Bearer " + token[0]));
		headers.add("Set-Cookie", String.format("rfToken=%s; Max-Age=604800; Path=/; HttpOnly=true;", token[1]));
		headers.setLocation(URI.create("http://localhost:8080/page/indexPage"));
		return new ResponseEntity<>(headers, HttpStatus.FOUND);
	}
}

//카카오 로그인 만들기 참고
//	//기존 로그인
// 	@PostMapping("/signin")
// 	public ResponseEntity<ApiResponse> signIn(@RequestBody LoginRequest loginRequest) {
// 		String[] token = userService.signIn(loginRequest).split(",");
// 		HttpHeaders headers = new HttpHeaders();
// 		headers.setBearerAuth(token[0]);
// 		headers.add("Set-Cookie", String.format("rfToken=%s; Max-Age=604800; Path=/; HttpOnly=true;", token[1]));
// 		return ResponseEntity.ok().headers(headers).body(ApiResponse.ok("로그인 완료"));
// 	}
//    @GetMapping("/kakao/callback")
//     public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
//         // code: 카카오 서버로부터 받은 인가 코드
//
//         String createToken = kakaoService.kakaoLogin(code, response);
//
//         // Cookie 생성 및 직접 브라우저에 Set
//         Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, createToken.substring(7));
//         cookie.setPath("/");
//         response.addCookie(cookie);
//
//         return "redirect:/api/shop";
//     }