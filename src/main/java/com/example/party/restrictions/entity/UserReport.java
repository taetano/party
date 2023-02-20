package com.example.party.restrictions.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.example.party.restrictions.dto.ReportUserRequest;
import com.example.party.restrictions.type.ReportReason;
import com.example.party.user.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class UserReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReportReason reason;
	@Column(nullable = false)
	private String details;
	@ManyToOne
	private User reporter;
	@Column(nullable = false)
	private Long userId;
	@Column(nullable = false)
	private Long reportUserId;
	@Column(nullable = false)
	private String reportUserEmail;

	public UserReport(User reporter, ReportUserRequest request, User user) {
		this.userId = reporter.getId();
		this.reportUserId = user.getId();
		this.reportUserEmail = user.getEmail();
		this.reason = request.getReason();
		this.details = request.getDetails();
	}
}
