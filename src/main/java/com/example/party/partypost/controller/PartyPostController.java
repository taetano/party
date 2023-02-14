package com.example.party.partypost.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.security.JwtUserDetailsService;
import com.example.party.partypost.Dto.PartyPostResponse;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.repository.PartyPostRepository;
import com.example.party.partypost.service.PartyPostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PartyPostController {

	private final PartyPostService partyPostService;
	private final PartyPostRepository partyPostRepository;

	@PostMapping("/likes/party-posts/{party_postId}")
	public DataResponseDto<String> toggleLikePartyPost(@PathVariable Long party_postId,
		@AuthenticationPrincipal UserDetails userDetails) {
		/*userDetails이용바람*/
		Long test_id = 0l; //임시 userDetails id값으로 바꾸기
		//좋아요 기능
		return partyPostService.toggleLikePartyPost(party_postId, test_id);
	}
}
