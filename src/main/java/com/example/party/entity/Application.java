package com.example.party.entity;

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
import javax.persistence.Table;

import com.example.party.enums.ApplicationStatus;
import com.example.party.common.TimeStamped;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
@Entity
public class Application extends TimeStamped {

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

	public Application(User user, PartyPost partyPost) {
		this.cancel = false;
		this.status = ApplicationStatus.PENDING;
		this.user = user;
		this.partyPost = partyPost;
		this.user.addApplication(this);
		this.partyPost.addApplication(this);
	}

	public boolean isWrittenByMe(Long userId) {
		return Objects.equals(this.user.getId(), userId);
	}

	public void cancel() {
		this.cancel = true;
	}
	public Long getWriterId() {
		return this.user.getId();
	}

	public String getWriterName() {
		return this.user.getNickname();
	}

	public String getWriterProfileImg() {
		return this.user.getProfileImg();
	}

	public int getWriterNoShowCnt() {
		return this.user.getNoShowCnt();
	}

	public boolean isSendByMe(Long userId) {
		return Objects.equals(this.partyPost.getWriterId(), userId);
	}

	public void accept() {
		this.status = ApplicationStatus.accept();
		this.user.increaseParticipationCnt();
		this.partyPost.increaseAcceptedCnt();
	}

	public void reject() {
		this.status = ApplicationStatus.reject();
	}

	public boolean isPending() {
		return this.status.isPending();
	}
}
