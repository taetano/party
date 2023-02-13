package com.example.party.partypost.entity;

import java.time.LocalDateTime;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.party.applicant.entity.Application;
import com.example.party.global.BaseEntity;
import com.example.party.partypost.type.Status;
import com.example.party.user.entity.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "party_post")
@Entity(name = "partPost")
public class PartyPost extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "title", nullable = false, length = 50)
	private String title;
	@Column(name = "content", nullable = false, columnDefinition = "TEXT")
	private String content;
	@Column(name = "view_cnt", nullable = false)
	private int viewCnt;
	@Column(name = "max_member", nullable = false)
	private byte maxMember;
	@Column(name = "is_active", nullable = false)
	private boolean active;

	// enum
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, columnDefinition = "ENUM('FINDING', 'FOUND', 'NO_SHOW_REPORTING', 'END')")
	private Status status;
	@Column(name = "close_date", nullable = false)
	private LocalDateTime closeDate;
	@Column(name = "party_date", nullable = false)
	private LocalDateTime partyDate;

	// 연관관계
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;
	@OneToMany(mappedBy = "partyPost")
	private List<Application> applications;

	public boolean isWrittenByMe(Long userId) {
		return Objects.equals(this.user.getId(), userId);
	}

	// TODO: API 1차 작업완료 후
	// 차단한 유저의 게시물 블라인드 처리 방식 생각
}
