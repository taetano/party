package com.example.party.partypost.controller;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.security.JwtUserDetailsService;
import com.example.party.partypost.Dto.PartyPostRequest;
import com.example.party.partypost.Dto.PartyPostResponse;
import com.example.party.partypost.service.IPartyPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PartyPostController {
  private final IPartyPostService iPartyPostService;

  //모집글 작성
  @PostMapping("/api/party-posts")
  public DataResponseDto<PartyPostResponse> createPartyPost(@RequestBody PartyPostRequest request,
      @AuthenticationPrincipal JwtUserDetailsService userDetailsService) {


  }
}
