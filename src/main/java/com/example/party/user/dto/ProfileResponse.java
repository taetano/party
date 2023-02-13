package com.example.party.user.dto;

public class ProfileResponse { // 예시
	private String email;
	private String nickName;
	private String phoneNum;
	private String proFileUrl;
	private String comment;
	private int participationCount;

	public ProfileResponse(String email, String nickName, String phoneNum, String proFileUrl, String comment,
		int participationCount) {
		this.email = email;
		this.nickName = nickName;
		this.phoneNum = phoneNum;
		this.proFileUrl = proFileUrl;
		this.comment = comment;
		this.participationCount = participationCount;
	}

}
