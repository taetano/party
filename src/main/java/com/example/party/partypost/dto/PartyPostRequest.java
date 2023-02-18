package com.example.party.partypost.dto;

import com.example.party.category.entity.Category;

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
	// private String categoryName;
	private Category category;
	private byte maxMember;
	private String partyDate;
	private String eubMyeonDong;
	private String address;
	private String detailAddress;
}
