package com.example.party.user.type;

public enum Status {
	ACTIVE, // 활성상태
	SUSPENDED, //블랙리스트
	DORMANT //회원탈퇴
	;

	public boolean isDormant() {
		return this == DORMANT;
	}

	public Status getDormant() {
		return DORMANT;
	}
}
