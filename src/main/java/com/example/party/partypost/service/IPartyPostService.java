package com.example.party.partypost.service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;

public interface IPartyPostService {
	DataResponseDto<?> createPartyPost();
	ListResponseDto<?> findPartyPosts();
	DataResponseDto<?> findOne();
	DataResponseDto<?> partyPostLike(); //모집게시물 좋아요

}
