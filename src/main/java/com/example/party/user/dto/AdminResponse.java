package com.example.party.user.dto;

import com.example.party.partypost.entity.PartyPost;
import com.example.party.restriction.entity.ReportPost;
import com.example.party.restriction.type.ReportReason;
import com.example.party.user.entity.User;
import lombok.Getter;

@Getter
public class AdminResponse {
    private ReportReason reason;
    private String details;
    private String title;
    private String content;
    private String nickname;
    private String email;
    private final String msg;

    public AdminResponse(User user, String msg) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.msg = msg;
    }

    public AdminResponse(User user, PartyPost partyPost, ReportPost reportPost, String msg) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.title = partyPost.getTitle();
        this.content = partyPost.getContent();
        this.reason = reportPost.getReason();
        this.details = reportPost.getDetailReason();
        this.msg = msg;
    }

    public AdminResponse(PartyPost partyPost, ReportPost reportPost, String msg) {
        this.title = partyPost.getTitle();
        this.content = partyPost.getContent();
        this.reason = reportPost.getReason();
        this.details = reportPost.getDetailReason();
        this.msg = msg;
    }
}
