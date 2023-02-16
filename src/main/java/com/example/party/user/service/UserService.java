package com.example.party.user.service;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.global.exception.LoginException;
import com.example.party.user.dto.LoginRequest;
import com.example.party.user.dto.MyProfileResponse;
import com.example.party.user.dto.OtherProfileResponse;
import com.example.party.user.dto.ProfileRequest;
import com.example.party.user.dto.SignupRequest;
import com.example.party.user.dto.WithdrawRequest;
import com.example.party.user.entity.Profile;
import com.example.party.user.entity.User;
import com.example.party.user.exception.EmailOverlapException;
import com.example.party.user.repository.ProfileRepository;
import com.example.party.user.repository.UserRepository;
import com.example.party.util.JwtProvider;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements IUserService {

	private static final String RT_TOKEN = "rTKey";
	private final UserRepository userRepository;
	private final ProfileRepository profileRepository;
	private final PasswordEncoder passwordEncoder;
	private final RedisTemplate<String, String> redisTemplate;

	//회원가입
	@Override
	public ResponseDto signUp(SignupRequest signupRequest) {
		Optional<User> users = userRepository.findByEmail(signupRequest.getEmail());
		if (users.isPresent()) {
			throw new EmailOverlapException();
		}

		String password = passwordEncoder.encode(signupRequest.getPassword());
		Profile profile = new Profile();
		profileRepository.save(profile);
		User user = new User(signupRequest, password, profile);
		userRepository.save(user);
		return ResponseDto.create("회원가입 완료");
	}

	//로그인
	@Override
	public String signIn(LoginRequest loginRequest) {
		User user = findByUser(loginRequest.getEmail());
		confirmPassword(loginRequest.getPassword(), user.getPassword());
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

		if (valueOperations.get(RT_TOKEN + user.getId()) != null) { // JwtVerificationFilter 45번째 줄
			redisTemplate.delete(RT_TOKEN + user.getId());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복 로그인");
		}

		String generateToken = JwtProvider.accessToken(user.getEmail(), user.getId());
		String refreshToken = JwtProvider.refreshToken(user.getEmail(), user.getId());

		valueOperations.set(RT_TOKEN + user.getId(), refreshToken);
		return generateToken + "," + refreshToken;
	}

	//로그아웃
	@Override
	public ResponseDto signOut(User user) {
		// 이후 security 에 url 보안 설정 필요합니다.
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		return ResponseDto.ok("회원 탈퇴 완료");
	}

	//회원탈퇴
	@Override
	public ResponseDto withdraw(User userDetails, WithdrawRequest withdrawRequest) {
		User user = findByUser(userDetails.getEmail());
		confirmPassword(withdrawRequest.getPassword(), user.getPassword());
		user.DormantState();
		return ResponseDto.ok("회원탈퇴 완료");
	}

	//프로필 수정
	public DataResponseDto<MyProfileResponse> updateProfile(ProfileRequest profileRequest, Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유저 찾기 실패")); //user 정보 조회
		user.updataProfile(profileRequest); //user 정보 수정
		MyProfileResponse myProfileResponse = new MyProfileResponse(user); // profile 내용 입력
		return DataResponseDto.ok("프로필 정보 수정 완료", myProfileResponse);

	}

	//내 프로필 조회
	public DataResponseDto<MyProfileResponse> getMyProfile(User user) {
		MyProfileResponse myProfileResponse = new MyProfileResponse(user); // profile 내용 입력
		return DataResponseDto.ok("내 프로필 조회", myProfileResponse);
	}

	//상대방 프로필 조회
	public DataResponseDto<OtherProfileResponse> getOtherProfile(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유저 찾기 실패")); //user 정보 조회
		OtherProfileResponse otherProfileResponse = new OtherProfileResponse(user); // profile 내용 입력
		return DataResponseDto.ok("타 프로필 조회", otherProfileResponse);
	}

	//private 메소드
	//repository에서 user 찾기
	private User findByUser(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(LoginException::new);
	}

	//비밀번호 확인
	private void confirmPassword(String requestPassword, String savedPassword) {
		if (!passwordEncoder.matches(requestPassword, savedPassword)) {
			throw new LoginException();
		}
	}
}
