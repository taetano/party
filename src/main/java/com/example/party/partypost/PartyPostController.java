package com.example.party.partypost;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.partypost.Dto.PartyPostListResponse;
import com.example.party.partypost.service.PartyPostService;
import com.example.party.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/party-posts")
@RestController
public class PartyPostController {

  private final PartyPostService partyPostService;

  //모집글전체조회
  @GetMapping("")
  public ResponseEntity<ListResponseDto<PartyPostListResponse>> findPartyList() {
    return ResponseEntity.ok(partyPostService.findPartyList());
  }

  //모집글 상세 조회(개별 상세조회)
  @GetMapping("/{party-postId}")
  public ResponseEntity<DataResponseDto> getPartyPost(@PathVariable Long partyPostId,
      @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(partyPostService.getPartyPost(partyPostId, user));
  }

  //모집글 삭제
  @DeleteMapping("/{party-postId}")
  public ResponseEntity<ResponseDto> deletePartyPost(@PathVariable Long partyPostId, @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(partyPostService.deletePartyPost(partyPostId,user));
  }


}
