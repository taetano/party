package com.example.party.partypost.service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;
import com.example.party.partypost.Dto.PartyPostListResponse;
import com.example.party.partypost.Dto.PartyPostRequest;
import com.example.party.partypost.Dto.PartyPostResponse;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.repository.PartyPostRepository;
import com.example.party.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartyPostService implements IPartyPostService {

  private final PartyPostRepository partyPostRepository;

  //모집글 작성
  @Override
  public DataResponseDto<PartyPostResponse> createPartyPost(User user, PartyPostRequest request) {

    //1. PartyPost 객체 생성
    PartyPost partyPost = new PartyPost(user, request.getTitle(), request.getContent(),
        request.getMaxMember(), request.getEubMyeonDong(), request.getAddress(), request.getDetailAddress(), request.getPartyDate());

    //2. repository에 저장
    partyPostRepository.save(partyPost);

    //3. partyPostResponse 생성
    PartyPostResponse partyPostResponse = new PartyPostResponse(partyPost);

    //4. DataResponseDto 생성 후 return
    return new DataResponseDto<>(HttpStatus.OK.value(), "모집글 작성 완료", partyPostResponse);
  }

  //모집글 수정
  @Override
  public DataResponseDto<PartyPostResponse> updatePartyPost(Long partyPostId, PartyPostRequest request) {

    //1. PartyPost 불러오기
    PartyPost partyPost = partyPostRepository.findById(partyPostId).orElseThrow(
        () -> new IllegalArgumentException("존재하지 않는 게시물입니다.")
    );

    //2. 수정할 내용 받기
    partyPost.update(request);

    //3. partyPostResponse 생성
    PartyPostResponse partyPostResponse = new PartyPostResponse(partyPost);

    //4. DataResponseDto 생성 후 return
    return new DataResponseDto<>(HttpStatus.OK.value(), "모집글 수정 완료", partyPostResponse);
  }

  //내가 작성한 모집글 조회 ( 내가 파티장인 경우만 )
  @Override
  public ListResponseDto<PartyPostListResponse> findMyCreatedPartyList(User user, int page) {
    Pageable pageable = PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.DESC, "modifiedAt"));

    //리스트 생성
    List<PartyPost> list = partyPostRepository.findByUserId(user, pageable);
    List<PartyPostResponse> myCreatedPartyList = list.stream()
        .filter(partyPost -> partyPost.getUserId().equals(user))
        .map(partyPost -> new PartyPostResponse(partyPost)).collect(Collectors.toList());
    return myCreatedPartyList;
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
