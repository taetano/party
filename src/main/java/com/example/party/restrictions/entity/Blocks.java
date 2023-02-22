package com.example.party.restrictions.entity;

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
public class Blocks {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// @Column(nullable = false)
	// private Long blockerId;
	// @Column(nullable = false)
	// private Long blockedId;

	@ManyToOne
	@JoinColumn(name = "blocker_id", nullable = false)
	private User blocker;
	@ManyToOne
	@JoinColumn(name = "blocked_id", nullable = false)
	private User blocked;

	public Blocks(User blocker, User blocked) {
		this.blocker = blocker;
		this.blocked = blocked;
	}

	public void addBlocks(User blocker) {
		// 양방향 연관 user block이랑 연결
		blocker.addRelation(this);
	}

	public void removeBlocks(User blocker) {
		blocker.removeRelation(this);
	}
}

