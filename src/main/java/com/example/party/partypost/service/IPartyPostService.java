package com.example.party.partypost.service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;
import com.example.party.partypost.Dto.PartyPostListResponse;
import com.example.party.partypost.Dto.PartyPostResponse;

public interface IPartyPostService {

	//모집글 작성
	DataResponseDto<PartyPostResponse> createPartyPost();

	//모집글 수정
	DataResponseDto<PartyPostResponse> updatePartyPost();

	//내가 작성한 모집글 리스트 조회
	ListResponseDto<PartyPostListResponse> getAllCreatePartyList();

	//내가 참석한 모집글 리스트 조회
	ListResponseDto<PartyPostListResponse> getAllJoinPartyList();
  
	DataResponseDto<?> partyPostLike(); //모집게시물 좋아요

	DataResponseDto<?> deletePartyPost(); //모집글 삭제
	ListResponseDto<?> getAllPartyList(); // 모집글전체조회
	DataResponseDto<?> showPartyPostDetail(); //모집글 상세 조회(개별 상세조회)


}
