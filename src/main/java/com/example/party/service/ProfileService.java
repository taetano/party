package com.example.party.service;

import com.example.party.common.ApiResponse;
import com.example.party.common.ItemApiResponse;
import com.example.party.dto.response.MyProfileResponse;
import com.example.party.dto.response.OtherProfileResponse;
import com.example.party.dto.request.ProfileRequest;
import com.example.party.entity.Profile;
import com.example.party.entity.User;
import com.example.party.exception.ExistNicknameException;
import com.example.party.exception.UserNotFoundException;
import com.example.party.repository.ProfileRepository;
import com.example.party.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public ApiResponse updateProfile(User user, ProfileRequest profileRequest, MultipartFile file)
            throws IOException {
        ProfileRequest uniqueInput = profileRequest.checkingInput(user);
        if (!uniqueInput.getNickname().equals(user.getNickname())) {
            if (userRepository.existsUserByNickname(profileRequest.getNickname())) {
                throw new ExistNicknameException();
            }
        }

        Profile profile = user.getProfile();
        profile.updateProfile(uniqueInput.getProfileImg(), uniqueInput.getComment());
        user.updateProfile(uniqueInput);

        // if (!file.isEmpty()) {
        // 	String storedFileName = s3Uploader.upload(file, "static");
        // 	profile.setProfileImg(storedFileName);
        // }

        profileRepository.save(profile);
        userRepository.save(user); //변경한 user 저장
        return ApiResponse.ok("프로필 정보 수정 완료"); //결과값 반환
    }

    //내 프로필 조회
    public ApiResponse getMyProfile(User user) {
        MyProfileResponse myProfileResponse = new MyProfileResponse(user); // profile 내용 입력
        return ItemApiResponse.ok("내 프로필 조회", myProfileResponse); //결과값 반환
    }

    //상대방 프로필 조회
    public ApiResponse getOtherProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new); //user 정보 조회
        OtherProfileResponse otherProfileResponse = new OtherProfileResponse(user); // profile 내용 입력
        return ItemApiResponse.ok("타 프로필 조회", otherProfileResponse); //결과값 반환
    }
}
