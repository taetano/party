package com.example.party.restriction.entity;

import javax.persistence.*;

import com.example.party.global.common.TimeStamped;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.user.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class NoShow extends TimeStamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reporter_id")
	private User reporter;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reported_id")
	private User reported;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "partyPost_id")
	private PartyPost partyPost;
	@Column(name = "no_show_cnt")
	private int noShowReportCnt;

	public NoShow(User reporter, User reported, PartyPost partyPost) {
		this.reporter = reporter;
		this.reported = reported;
		this.partyPost = partyPost;
		this.noShowReportCnt = 0;
	}

	public int getNoShowReportCnt() {
		return noShowReportCnt;
	}

	public void PlusNoShowReportCnt() {
		this.noShowReportCnt+=1;
	}
}
