package com.example.party.partypost.service;

import org.springframework.stereotype.Service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.repository.PartyPostRepository;
import com.example.party.user.dto.MyProfileResponse;
import com.example.party.user.dto.ProfileRequest;
import com.example.party.user.entity.User;
import com.example.party.user.repository.UserRepository;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@Service
public class PartyPostService {
	private UserRepository userRepository;

	private PartyPostRepository partyPostRepository;

	public DataResponseDto<String> toggleLikePartyPost(Long party_postId, Long userId) {
		//모집글 찾기
		PartyPost partyPost = partyPostRepository.findById(party_postId).orElseThrow(
			() -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
		);
		String partPostTitle = partyPost.getTitle(); //모집글 제목 입력
		//유저 찾기
		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 유저가 존재 하지 않습니다.")
		);

		//좋아요 확인
		if (!(user.getLikePartyPosts().add(partyPost))) {
			user.getLikePartyPosts().remove(partyPost);
			return new DataResponseDto(200, "모집글 좋아요 취소 완료", partPostTitle);
		} else {
			return new DataResponseDto(200, "모집글 좋아요 완료", partPostTitle);
		}
	}

}
