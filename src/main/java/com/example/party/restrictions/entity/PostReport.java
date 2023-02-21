package com.example.party.restrictions.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.example.party.partypost.entity.PartyPost;
import com.example.party.restrictions.dto.ReportPostRequest;
import com.example.party.restrictions.type.ReportReason;
import com.example.party.user.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class PostReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReportReason reason;
	@Column(nullable = false)
	private String details;
	@ManyToOne
	private User user;
	@ManyToOne
	private PartyPost reportPost;

	public PostReport(User user, ReportPostRequest request, PartyPost post) {
		this.user = user;
		this.reason = request.getResponse().getReason();
		this.details = request.getDetails();
		this.reportPost = post;
	}
}
