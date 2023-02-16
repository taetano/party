package com.example.party.partypost.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartyPostRequest {

	private String title;
	private String content;
	private byte maxMember;
	private LocalDateTime partyDate;
	private String eubMyeonDong;
	private String address;
	private String detailAddress;
}
