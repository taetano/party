package com.example.party.user.service;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.user.dto.LoginRequest;
import com.example.party.user.dto.ProfileResponse;
import com.example.party.user.dto.SignupRequest;
import com.example.party.user.dto.WithdrawRequest;
import com.example.party.user.entity.User;
import com.example.party.user.repository.UserRepository;
import com.example.party.user.type.Status;
import com.example.party.user.type.UserRole;
import com.example.party.util.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;


	private User findByUser(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일"));
	}

	private void confirmPassword(String requestPassword, String savedPassword) {
		if(!passwordEncoder.matches(requestPassword, savedPassword))
		{ throw new IllegalArgumentException("비밀번호 불일치"); }
	}

	@Override
	public ResponseDto signUp(SignupRequest signupRequest) {
		Optional<User> users = userRepository.findByEmail(signupRequest.getEmail());
		if (users.isPresent()) { throw new IllegalArgumentException("유저가 존재합니다"); }

		String userEmail = signupRequest.getEmail();
		String password = passwordEncoder.encode(signupRequest.getPassword());
		User user = new User(userEmail, password, signupRequest.getNickname(), signupRequest.getPhoneNum()
			, UserRole.ROLE_USER, Status.ACTIVE);
		userRepository.save(user);
		return new ResponseDto(201, "회원가입 완료");
	}

	@Override
	public ResponseDto signIn(LoginRequest loginRequest, HttpServletResponse response) {
		User user = findByUser(loginRequest.getEmail());
		confirmPassword(loginRequest.getPassword(), user.getPassword());
		String generateToken = JwtProvider.generateToken(user);
		response.addHeader(jwtProvider.AUTHORIZATION_HEADER, generateToken);
		return new ResponseDto(200, "로그인 완료");
	}

	@Override
	public ResponseDto signOut(User user) {
		return null;
	}

	@Override
	public ResponseDto withdraw(User userDetails, WithdrawRequest withdrawRequest) {
		User user = findByUser(userDetails.getEmail());
		confirmPassword(withdrawRequest.getPassword(), user.getPassword());
		user.changeDORMANT();
		return new ResponseDto(200, "회원탈퇴 완료");
	}

	@Override
	public DataResponseDto<ProfileResponse> updateProfile() {
		return null;
	}

	@Override
	public DataResponseDto<ProfileResponse> getMyProfile() {
		return null;
	}

	@Override
	public DataResponseDto<ProfileResponse> getOtherProfile() {
		return null;
	}
}
