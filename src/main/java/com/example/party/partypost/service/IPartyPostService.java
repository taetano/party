package com.example.party.partypost.service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.partypost.Dto.PartyPostListResponse;
import com.example.party.partypost.Dto.PartyPostResponse;
import com.example.party.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface IPartyPostService {

	//모집글 작성
	DataResponseDto<PartyPostResponse> createPartyPost();

	//모집글 수정
	DataResponseDto<PartyPostResponse> updatePartyPost();

	//내가 작성한 모집글 리스트 조회 ( 내가 파티장인 경우만 )
	ListResponseDto<PartyPostListResponse> findMyCreatedPartyList();

	//내가 참석한 모집글 리스트 조회( 내가 파티원인 경우만 )
	ListResponseDto<PartyPostListResponse> findMyJoinedPartyList();

	//모집게시물 좋아요 (*좋아요 취소도 포함되는 기능임)
	DataResponseDto<?> toggleLikePartyPost();

	//모집글 삭제
	DataResponseDto<?> deletePartyPost();

	//모집글전체조회
	ListResponseDto<?> findPartyList();

	//모집글 상세 조회(개별 상세조회)
	DataResponseDto<?> getPartyPost();


	//모집글 상세 조회(개별 상세조회)
	@Transactional
	DataResponseDto<PartyPostResponse> getPartyPost(Long postId);

	//모집글 상세 조회(개별 상세조회)
	@Transactional
	DataResponseDto<PartyPostResponse> getPartyPost(Long postId, User user);

  //모집글 삭제
  ResponseDto deletePartyPost(Long partyPostId, User user);
}
