package com.example.party.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.party.user.dto.LoginRequest;
import com.example.party.user.dto.SignupRequest;
import com.example.party.user.service.UserService;
import com.example.party.util.JwtProvider;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/api/users")
public class UserController {
	private final UserService userService;
	private final JwtProvider jwtProvider;

	@PostMapping("/signup")
	public ResponseEntity creatUser(@RequestBody @Valid SignupRequest signupRequest) {
		userService.signUp(signupRequest);
		return new ResponseEntity<>("회원가입 성공", HttpStatus.CREATED);
	}

	@PostMapping("/signin")
	public ResponseEntity LoginUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		userService.signIn(loginRequest);
		response.addHeader(jwtProvider.AUTHORIZATION_HEADER,"미완성");
		return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
	}
}
