package com.example.party.partypost.service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;
import com.example.party.partypost.Dto.PartyPostListResponse;
import com.example.party.partypost.Dto.PartyPostResponse;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.repository.PartyPostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

public class PartPostService implements IPartyPostService {

  private PartyPostRepository partyPostRepository;

  @Override
  public DataResponseDto<PartyPostResponse> createPartyPost() {
    return null;
  }

  @Override
  public DataResponseDto<PartyPostResponse> updatePartyPost() {
    return null;
  }

  @Override
  public ListResponseDto<PartyPostListResponse> findMyCreatedPartyList() {
    return null;
  }

  @Override
  public ListResponseDto<PartyPostListResponse> findMyJoinedPartyList() {
    return null;
  }

  @Override
  public DataResponseDto<?> toggleLikePartyPost() {
    return null;
  }

  @Override
  public DataResponseDto<?> deletePartyPost() {
    return null;
  }

  //모집글 전체 조회
  @Override
  @Transactional
  public ListResponseDto<?> findPartyList() {
    // 모집글 전체 불러오기
    // DTO의 LIST 생성
    // ListResponseDto 생성
    return null;
  }

  //모집글 상세 조회(개별 상세조회)
  @Override
  @Transactional
  public DataResponseDto<PartyPostResponse> getPartyPost(Long postId) {
    //1. Repo 에서 특정 post id의 partyPost 가져와
    PartyPost partyPost = partyPostRepository.findById(postId)
        .orElseThrow(() -> new IllegalArgumentException("해당하는 postId의 partyPost가 존재하지 않습니다."));

    //2. PartyPostResponse Dto 생성
    PartyPostResponse partyPostResponse = new PartyPostResponse(partyPost);

    //3. DataResponseDto 생성하고 (2) 를 넣어준 후 return
    return new DataResponseDto<>(HttpStatus.OK.value(),"모집글 상세 조회 완료", partyPostResponse );

  }
}
