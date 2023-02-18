package com.example.party.restrictions.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.party.user.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Block {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "blocker_id", nullable = false)
	private User blocker;
	private Long blockerId;

	@ManyToOne
	@JoinColumn(name = "blocked_id", nullable = false)
	private User blocked;
	@Column(nullable = false)
	private Long blockedId;
	private String blockedEmail;

	public Block(User blocker, User blocked) {
		this.blockerId = blocker.getId();
		this.blockedId = blocked.getId();
		this.blockedEmail = blocked.getEmail();
	}

	public User getBlocker() {
		return blocker;
	}

	public User getBlocked() {
		return blocked;
	}
}

