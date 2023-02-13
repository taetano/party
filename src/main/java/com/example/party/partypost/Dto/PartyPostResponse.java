package com.example.party.partypost.Dto;

import com.example.party.applicant.entity.Application;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.type.Status;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class PartyPostResponse {

  //id
  private final Long id;
  //title
  private final String title;
  //content
  private final String content;
  //status
  private final Status status;
  //maxmember
  private final byte maxMember;
  //partydate
  private final LocalDateTime partyDate;
  //day (직접계산해서)
  private final String dayOfWeek;
  //sigungu
  private final String sigungu;
  //detailAddress
  private final String detailAddress;
  //plaice
  private final String plaice;
  //applications(userid, nickname, profileImg)
  private final List<ApplicationResponse> applications;

  //생성자
  PartyPostResponse(PartyPost partyPost) {
    this.id = partyPost.getId();
    this.title = partyPost.getTitle();
    this.content = partyPost.getContent();
    this.status = partyPost.getStatus();
    this.maxMember = partyPost.getMaxMember();
    this.partyDate = partyPost.getPartyDate();
    this.dayOfWeek = whichDayOfWeek(partyPost.getPartyDate());
    this.sigungu = "임시 시/군/구";
    this.detailAddress = "임시 detailAddress";
    this.plaice = "임시 pliace";
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
