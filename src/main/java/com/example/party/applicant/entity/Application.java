package com.example.party.applicant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.party.applicant.type.ApplicationStatus;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.user.entity.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
@Entity
public class Application {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "is_cancel", nullable = false)
	private boolean cancel;

	// enum
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, columnDefinition = "ENUM('PENDING', 'ACCEPT', 'REJECT')")
	private ApplicationStatus status;

	// 연관관계
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;
	@ManyToOne(optional = false)
	@JoinColumn(name = "party_post_id")
	private PartyPost partyPost;

	//MODIFIED_AT 필요
}
