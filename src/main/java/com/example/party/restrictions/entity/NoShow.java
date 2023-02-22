package com.example.party.restrictions.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.example.party.partypost.entity.PartyPost;
import com.example.party.user.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class NoShow {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private User reporter;
	@ManyToOne
	private User reported;
	@ManyToOne
	private PartyPost post;
	private int noShowReportCnt;

	public NoShow(User reporter, PartyPost post, User reported) {
		this.reporter = reporter;
		this.reported = reported;
		this.post = post;
		this.noShowReportCnt = 0;
	}

	public int getNoShowReportCnt() {
		return noShowReportCnt;
	}

	public void PlusNoShowReportCnt() {
		this.noShowReportCnt+=1;
	}
}
