package com.example.party.partypost.service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.partypost.Dto.PartyPostListResponse;
import com.example.party.partypost.Dto.PartyPostResponse;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.repository.PartyPostRepository;
import com.example.party.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class PartyPostService implements IPartyPostService {

  private final PartyPostRepository partyPostRepository;

  //모집글 전체 조회

  @Override
  @Transactional
  public ListResponseDto<PartyPostListResponse> findPartyList() {
    // 1.모집글 전체 불러오기
    List<PartyPost> partyPostList = partyPostRepository.findAllByOrderById(); //modifiedAt 순으로 수정 필요
    // 2.DTO의 LIST 생성
    List<PartyPostListResponse> partyPostDtoList = partyPostList.stream().map(
        PartyPostListResponse::new).collect(
        Collectors.toList());
    // 3.ListResponseDto 생성 후 리턴
    return new ListResponseDto<>(HttpStatus.OK.value(), "모집글 조회 완료", partyPostDtoList);
  }
  //모집글 상세 조회(개별 상세조회)
  @Override
  @Transactional
  public DataResponseDto<PartyPostResponse> getPartyPost(Long partyPostId, User user) {
    //1. partyPostId의 partyPost 를 가져오기
    PartyPost partyPost = partyPostRepository.findById(partyPostId)
        .orElseThrow(() -> new IllegalArgumentException("해당하는 postId의 partyPost 가 존재하지 않습니다."));

    //2. 조회자!=작성자 인경우 조회한 partyPost의 조회수 올려주기
    partyPost.increaseViewCnt(user);

    //2. PartyPostResponse Dto 생성
    PartyPostResponse partyPostResponse = new PartyPostResponse(partyPost);

    //3. DataResponseDto 생성하고 (2) 를 넣어준 후 return
    return new DataResponseDto<>(HttpStatus.OK.value(), "모집글 상세 조회 완료", partyPostResponse);
  }

  //모집글 삭제
  @Override
  public ResponseDto deletePartyPost(Long partyPostId, User user) {
    //1. partyPost 객체 가져오기
    PartyPost partyPost = partyPostRepository.findById(partyPostId).orElseThrow(
        () -> new IllegalArgumentException("해당 partyPost가 존재하지 않습니다")
    );
    //2. 삭제 가능한지 확인 (작성자인지 / 모집마감전인지 / 참가신청한 모집자가 없는지)
    canDeletePartyPost(user,partyPost);

    //3. 2가 통과한 경우 삭제 진행
    partyPost.deletePartyPost();
    return new ResponseDto(HttpStatus.OK.value(), "모집글 상세 조회 완료");
  }

  //삭제가능여부 확인
  private void canDeletePartyPost(User user, PartyPost partyPost){
    //1. 작성자인지 확인
    if(!partyPost.isWriter(user)){
      throw new IllegalArgumentException("작성자만 모집글을 삭제할 수 있습니다");
    };
    //2. 모집마감전인지 확인
    if(!partyPost.beforeCloseDate(LocalDateTime.now())){
      throw new IllegalArgumentException("모집마감시간이 지나면 모집글을 삭제할 수 없습니다");
    }
    //3. 참가신청한 모집자가 없는지 확인
    if(!partyPost.haveNoApplications()){
      throw new IllegalArgumentException("참가신청자가 있는 경우 삭제할 수 없습니다");
    }
  }
}
