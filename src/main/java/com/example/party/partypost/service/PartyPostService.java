package com.example.party.partypost.service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;
import com.example.party.partypost.Dto.PartyPostListResponse;
import com.example.party.partypost.Dto.PartyPostRequest;
import com.example.party.partypost.Dto.PartyPostResponse;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartyPostService implements IPartyPostService {

  private final PartyPostRepositorydd partyPostRepository;
  private final UserRepository userRepository;

  //모집글 작성
  @Override
  public PartyPostResponse createPartyPost(Long userId, PartyPostRequest request) {
    userRepository.findByUserId(userId).orElseThrow(
        () -> new IllegalArgumentException("존재하지 않는 회원입니다.")
    );

    PartyPost partyPost = new PartyPost(userId, request.getTitle(), request.getContent(),
        request.getMaxMember(),request.getPartyDate(), request.getEubMyeonDong(),
        request.getAddress(), request.getDetailAddress());
    partyPostRepository.save(partyPost);
    return new PartyPostResponse(partyPost);
  }

  //모집글 수정
  @Override
  public DataResponseDto<PartyPostResponse> updatePartyPost() {
    return null;
  }

  //내가 작성한 모집글 조회 ( 내가 파티장인 경우만 )
  @Override
  public ListResponseDto<PartyPostListResponse> findMyCreatedPartyList() {
    return null;
  }

  //내가 참석한 모집글 조회 ( 내가 파티원인 경우만 )
  @Override
  public ListResponseDto<PartyPostListResponse> findMyJoinedPartyList() {
    return null;
  }

  //모집게시물 좋아요 (*좋아요 취소도 포함되는 기능임)
  @Override
  public DataResponseDto<?> toggleLikePartyPost() {
    return null;
  }

  //모집글 삭제
  @Override
  public DataResponseDto<?> deletePartyPost() {
    return null;
  }

  //모집글전체조회
  @Override
  public ListResponseDto<?> findPartyList() {
    return null;
  }

  //모집글 상세 조회(개별 상세조회)
  @Override
  public DataResponseDto<?> getPartyPost() {
    return null;
  }

}
