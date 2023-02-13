package com.example.party.applicant.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.party.applicant.type.ApplicationStatus;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.user.entity.User;

import lombok.Getter;

@Getter
@Entity
public class Application {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "is_cancel", nullable = false)
	private boolean cancel;
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updated_at;

	// enum
	@Enumerated(EnumType.STRING)
	@Column(name = "result", nullable = false, columnDefinition = "ENUM('ACCEPT', 'REJECT')")
	private ApplicationStatus status;

	// 연관관계
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;
	@ManyToOne(optional = false)
	@JoinColumn(name = "party_post_id")
	private PartyPost partyPost;

	public boolean canCancel(Long userId) {
		return Objects.equals(this.user.getId(), userId);
	}

	public void cancel() {
		this.cancel = true;
	}

	public String getNickname() {
		return this.user.getNickname();
	}

	public String getProfileImg() {
		return this.user.getProfileImg();
	}

	public int getNoShowCnt() {
		return this.user.getNoShowCnt();
	}

	public boolean canModify(Long userId) {
		return Objects.equals(this.partyPost.getUser().getId(), userId);
	}

	public void accept() {
		if (this.status != ApplicationStatus.PENDING) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바르지 않은 접근입니다.");
		}

		this.status = ApplicationStatus.ACCEPT;
		this.updated_at = LocalDateTime.now();
		this.user.increaseParticipationCnt();
	}

	public void reject() {
		if (this.status != ApplicationStatus.PENDING) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바르지 않은 접근입니다.");
		}

		this.status = ApplicationStatus.REJECT;
		this.updated_at = LocalDateTime.now();
	}
}
