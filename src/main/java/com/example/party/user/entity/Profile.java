package com.example.party.user.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Profile")
public class Profile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "img")
	private String img;
	@Column(name = "comment")
	private String comment;
	@Column(name = "no_show_cnt", nullable = false)
	private int noShowCnt;

	@Column(name = "participation_cnt", nullable = false)
	private int participationCnt;
	private int adminReportCnt;

	public void setId(Long id) {
		this.id = id;
	}

	public Profile(String img, String comment, int participationCnt) {
		this.img = img;
		this.comment = comment;
		this.noShowCnt = 0;
		this.participationCnt = participationCnt;
		this.adminReportCnt = 0;
	}

	public void updateProfile(String img, String comment) {
		this.img = img;
		this.comment = comment;
	}

	public void plusAdminReportCnt() { this.adminReportCnt += 1; }
	public void plusNoShowCnt() {
		this.noShowCnt += 1;
	}

	public void increaseParticipationCnt() {
		this.participationCnt = this.participationCnt + 1;
	}

}
