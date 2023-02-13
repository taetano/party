package com.example.party.user.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.global.security.JwtUserDetailsService;
import com.example.party.user.dto.LoginRequest;
import com.example.party.user.dto.ProfileRequest;
import com.example.party.user.dto.ProfileResponse;
import com.example.party.user.dto.SignupReqest;
import com.example.party.user.entity.Profile;
import com.example.party.user.entity.User;
import com.example.party.user.repository.ProfileRepository;
import com.example.party.user.repository.UserRepository;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@Service
public class UserService implements IUserService {

	private UserRepository userRepository;
	private ProfileRepository profileRepository;

	@Override
	public ResponseDto signUp(SignupReqest signupReqest) {
		return null;
	}

	@Override
	public ResponseDto signIn(LoginRequest loginRequest) {
		return null;
	}

	@Override
	public ResponseDto signOut(User user) {
		return null;
	}

	@Override
	public ResponseDto withdraw(User user) {
		return null;
	}

	public DataResponseDto<ProfileResponse> updateProfile(ProfileRequest profileRequest, Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유저 찾기 실패")); //user 정보 조회
		user.updataProfile(profileRequest.getNickName(), profileRequest.getPhoneNum()); //user 정보 수정

		Long profileId = user.getProfile().getId(); // 유저 정보를 이용한 프로필 id 찾기
		Profile profile = profileRepository.findById(profileId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "프로필 찾기 실패")); // 프로필 정보 조회
		profile.updateProfile(profileRequest.getProFileUrl(), profileRequest.getComment()); // 프로필 정보 수정
		ProfileResponse profileResponse = new ProfileResponse(user, profile); // profile 내용 입력
		return new DataResponseDto(200, "프로필 정보 수정 완료", profileResponse);

	}

	public DataResponseDto<ProfileResponse> getMyProfile(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유저 찾기 실패")); //user 정보 조회
		Long profileId = user.getProfile().getId(); // 유저 정보를 이용한 프로필 id 찾기
		Profile profile = profileRepository.findById(profileId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "프로필 찾기 실패")); // 프로필 정보 조회
		ProfileResponse profileResponse = new ProfileResponse(user, profile); // profile 내용 입력
		return new DataResponseDto(200, "프로필 조회", profileResponse);
	}


	public DataResponseDto<ProfileResponse> getOtherProfile(Long id){
		User user = userRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유저 찾기 실패")); //user 정보 조회
		Long profileId = user.getProfile().getId(); // 유저 정보를 이용한 프로필 id 찾기
		Profile profile = profileRepository.findById(profileId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "프로필 찾기 실패")); // 프로필 정보 조회
		ProfileResponse profileResponse = new ProfileResponse(user, profile); // profile 내용 입력
		return new DataResponseDto(200, "프로필 조회", profileResponse);
	}
}
