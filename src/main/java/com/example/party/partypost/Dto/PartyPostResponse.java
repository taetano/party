package com.example.party.partypost.Dto;

import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.type.Status;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PartyPostResponse {
  private Long partyPostId;
  private String title;
  private String content;
  private int viewCnt;
  private Status status;
  private LocalDateTime partyDate;
  private LocalDateTime closeDate;
  private String day;
  private byte maxMember;
  private String eubMyeonDong;
  private String address;
  private String detailAddress;
  private String joinMember;

  public PartyPostResponse(PartyPost partyPost) {
    this.partyPostId = partyPost.getPartyPostId();
    this.title = partyPost.getTitle();
    this.content = partyPost.getContent();
    this.viewCnt = partyPost.getViewCnt();
    this.status = partyPost.getStatus();
    this.partyDate = partyPost.getPartyDate();
    this.closeDate = partyPost.getCloseDate();
    this.day = partyPost.getDay();
    this.maxMember = partyPost.getMaxMember();
    this.eubMyeonDong = partyPost.getEubMyeonDong();
    this.address = partyPost.getAddress();
    this.detailAddress = partyPost.getDetailAddress();
    this.joinMember = partyPost.getJoinMember();
  }
}
