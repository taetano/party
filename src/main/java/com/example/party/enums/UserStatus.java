package com.example.party.enums;

public enum UserStatus {
	ACTIVE, // 활성상태
	SUSPENDED, //블랙리스트
	DORMANT //회원탈퇴
	;

	public boolean isDormant() {
		return this == DORMANT;
	}

	public UserStatus getDormant() {
		return DORMANT;
	}
}
