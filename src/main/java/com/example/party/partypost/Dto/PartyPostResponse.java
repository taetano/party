package com.example.party.partypost.Dto;

import com.example.party.applicant.type.ApplicationResponse;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.type.Status;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PartyPostResponse {
  private Long id;
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
  private List<ApplicationResponse> joinMember;

  public PartyPostResponse(PartyPost partyPost) {
    this.id = partyPost.getId();
    this.title = partyPost.getTitle();
    this.content = partyPost.getContent();
    this.viewCnt = partyPost.getViewCnt();
    this.status = partyPost.getStatus();
    this.partyDate = partyPost.getPartyDate();
    this.closeDate = partyPost.getCloseDate();
    this.day = partyDate.getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREAN);
    this.maxMember = partyPost.getMaxMember();
    this.eubMyeonDong = partyPost.getEubMyeonDong();
    this.address = partyPost.getAddress();
    this.detailAddress = partyPost.getDetailAddress();
    this.joinMember = partyPost.getApplications().stream().map(
        ApplicationResponse::new).collect(Collectors.toList());
  }
}
