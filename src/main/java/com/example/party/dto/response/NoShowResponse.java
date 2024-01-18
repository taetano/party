package com.example.party.dto.response;

import com.example.party.entity.User;
import lombok.Getter;

@Getter
public class NoShowResponse {
    private final Long userId;
    private final String email;
    private final int noShowCnt;

    public NoShowResponse(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.noShowCnt = user.getProfile().getNoShowCnt();
    }
}
