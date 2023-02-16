package com.example.party.partypost.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

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
	private String partyDate;
	private String eubMyeonDong;
	private String address;
	private String detailAddress;
}
