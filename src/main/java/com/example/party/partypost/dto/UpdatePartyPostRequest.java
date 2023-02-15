package com.example.party.partypost.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdatePartyPostRequest {

	//제목, 상세내용, 카테고리, 주소만 변경 가능 /현재 모임시작시간 & 모임마감시간 & 모집인원 변경 불가능
	private String title;
	private String content;
	private String eubMyeonDong;
	private String address;
	private String detailAddress;
}
