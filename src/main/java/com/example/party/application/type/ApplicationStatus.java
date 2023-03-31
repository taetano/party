package com.example.party.application.type;

public enum ApplicationStatus {
	PENDING, //보류중
	ACCEPT, //수락
	REJECT //거부
	;

	public static ApplicationStatus accept() {
		return ACCEPT;
	}

	public static ApplicationStatus reject() {
		return REJECT;
	}

	public boolean isPending() {
		return this == PENDING;
	}

}
