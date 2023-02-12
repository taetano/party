package com.example.party.apply.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.example.party.apply.type.ApplyResult;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.user.entity.User;

import lombok.Getter;

@Getter
@Entity
public class Apply {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "is_cancel", nullable = false)
	private boolean cancel;

	// enum
	@Enumerated(EnumType.STRING)
	@Column(name = "result", nullable = false, columnDefinition = "ENUM('ACCEPT', 'REJECT')")
	private ApplyResult result;

	// 연관관계
	@ManyToOne
	private User user;
	@ManyToOne
	private PartyPost partyPost;
}
