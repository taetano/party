package com.example.party.dto.request;

import lombok.Getter;

@Getter
public class NoShowRequest {
	private Long userId;
	private Long partyPostId;

    public int getMinusValue() {
        return 0;
    }
}
