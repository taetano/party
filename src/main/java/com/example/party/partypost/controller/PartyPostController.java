package com.example.party.partypost.controller;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.partypost.Dto.PartyPostRequest;
import com.example.party.partypost.Dto.PartyPostResponse;
import com.example.party.partypost.service.PartyPostService;
import com.example.party.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PartyPostController {
  private final PartyPostService partyPostService;

  //모집글 작성
  @PostMapping("/api/party-posts")
  public ResponseEntity<DataResponseDto<PartyPostResponse>> createPartyPost(@RequestBody PartyPostRequest request,
      @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(partyPostService.createPartyPost(user, request));
  }
}
