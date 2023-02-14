package com.example.party.partypost.service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;
import com.example.party.partypost.Dto.PartyPostListResponse;
import com.example.party.partypost.Dto.PartyPostResponse;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.repository.PartyPostRepository;
import com.example.party.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

public class PartyPostService implements IPartyPostService {

  private PartyPostRepository partyPostRepository;

  //모집글 전체 조회
  @Override
  @Transactional
  public ListResponseDto<PartyPostListResponse> findPartyList() {
    // 모집글 전체 불러오기
    List<PartyPost> partyPostList = partyPostRepository.findAllByOrderById(); //modifiedAt 순으로 수정 필요
    // DTO의 LIST 생성
    List<PartyPostListResponse> partyPostDtoList = partyPostList.stream().map(
        PartyPostListResponse::new).collect(
        Collectors.toList());
    // ListResponseDto 생성 후 리턴
    return new ListResponseDto<>(HttpStatus.OK.value(), "모집글 조회 완료", partyPostDtoList);
  }

  //모집글 상세 조회(개별 상세조회)
  @Override
  @Transactional
  public DataResponseDto<PartyPostResponse> getPartyPost(Long postId, User user) {
    //1. Repo 에서 특정 post id의 partyPost 가져와
    PartyPost partyPost = partyPostRepository.findById(postId)
        .orElseThrow(() -> new IllegalArgumentException("해당하는 postId의 partyPost 가 존재하지 않습니다."));

    //2. 조회자!=작성자 인경우 조회한 partyPost의 조회수 올려주기
    partyPost.increaseViewCnt(user);

    //2. PartyPostResponse Dto 생성
    PartyPostResponse partyPostResponse = new PartyPostResponse(partyPost);

    //3. DataResponseDto 생성하고 (2) 를 넣어준 후 return
    return new DataResponseDto<>(HttpStatus.OK.value(), "모집글 상세 조회 완료", partyPostResponse);
  }
}
