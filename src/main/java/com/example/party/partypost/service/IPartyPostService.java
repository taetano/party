package com.example.party.partypost.service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;

public interface IPartyPostService {
	DataResponseDto<?> createPartyPost();
	ListResponseDto<?> findPartyPosts(); // 전체모집글 조회
	DataResponseDto<?> viewPartyPost(); // 개별 모집글 조회
	DataResponseDto<?> partyPostLike(); //모집게시물 좋아요

	DataResponseDto<?> deletePartyPost(); //모집글 삭제


}
