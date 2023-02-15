package com.example.party.partypost.dto;

import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.type.Status;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PartyPostListResponse {

  //title
  private final String title;
  //status
  private final Status status;
  //createdAt
  private final LocalDateTime createdAt;
  //modifiedAt
  private final LocalDateTime modifiedAt;
  //maxMember
  private final byte maxMember;
  //partyDate
  private final LocalDateTime partyDate;
  //address
  private final String address;

  public PartyPostListResponse(PartyPost partyPost) {
    this.title = partyPost.getTitle();
    this.status = partyPost.getStatus();
    this.createdAt = partyPost.getCreatedAt();
    this.modifiedAt = partyPost.getModifiedAt();
    this.maxMember = partyPost.getMaxMember();
    this.partyDate = partyPost.getPartyDate();
    this.address = partyPost.getAddress();
  }
}
