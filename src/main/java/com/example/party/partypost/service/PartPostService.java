package com.example.party.partypost.service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;
import com.example.party.partypost.Dto.PartyPostListResponse;
import com.example.party.partypost.Dto.PartyPostResponse;
import com.example.party.partypost.repository.PartyPostRepository;

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
  public ListResponseDto<?> findPartyList() {
    // 모집글 전체 불러오기
    // DTO의 LIST 생성
    // ListResponseDto 생성
    return null;
  } // 청소기 만 돌리고 올게여 15분! (3:50~

  @Override
  public DataResponseDto<?> getPartyPost() {
    return null;
  }
}
