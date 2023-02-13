package com.example.party.partypost.Dto;

import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.type.Status;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class PartyPostResponse {
  private final Long id;
  private final String title;
  private final String content;
  private final Status status;
  private final LocalDateTime createdAt;
  private final LocalDateTime modifiedAt;
  private final byte maxMember;
  private final LocalDateTime partyDate;
  private final String dayOfWeek; //partyDate 로 직접 계산 (ex) 토
  private final String sigungu;
  private final String detailAddress;
  private final String place;
  private final List<ApplicationResponse> applications; //applications(userid, nickname, profileImg)

  //생성자
  public PartyPostResponse(PartyPost partyPost) {
    this.id = partyPost.getId();
    this.title = partyPost.getTitle();
    this.content = partyPost.getContent();
    this.status = partyPost.getStatus();
    this.createdAt = LocalDateTime.now(); // 임시값
    this.modifiedAt = LocalDateTime.now(); // 임시값
    this.maxMember = partyPost.getMaxMember();
    this.partyDate = partyPost.getPartyDate();
    this.dayOfWeek = whichDayOfWeek(partyPost.getPartyDate());
    this.sigungu = "임시 시/군/구";
    this.detailAddress = "임시 detailAddress";
    this.place = "임시 place";
    this.applications = makeApplications(partyPost);

  }

  //partyDate 를 넣어주면 요일을 계산해주는 메소드 (ex) 토
  private String whichDayOfWeek(LocalDateTime partyDate) {
    return partyDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
  }

  //PartyPost Entity의 List<Application> 을 applicationDto 로 바꿔주는 메소드
  private List<ApplicationResponse> makeApplications(PartyPost partyPost) {

    return partyPost.getApplications().stream().map(
        ApplicationResponse::new).collect(
        Collectors.toList());
  }

}
