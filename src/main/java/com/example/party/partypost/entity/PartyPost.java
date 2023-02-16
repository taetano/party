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

import com.example.party.application.entity.Application;
import com.example.party.global.BaseEntity;
import com.example.party.partypost.dto.PartyPostRequest;
import com.example.party.partypost.dto.UpdatePartyPostRequest;
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
	private byte maxMember; // MYSQL DB 상에
	@Column(name = "eub_myeon_dong", nullable = false)
	private String eubMyeonDong;
	@Column(name = "address", nullable = false)
	private String address;
	@Column(name = "detail_address", nullable = false)
	private String detailAddress;
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

	//생성자
	public PartyPost(User user, PartyPostRequest request, LocalDateTime partyDate) {
		this.user = user;
		this.title = request.getTitle();
		this.content = request.getContent();
		this.maxMember = request.getMaxMember();
		this.eubMyeonDong = request.getEubMyeonDong();
		this.address = request.getAddress();
		this.detailAddress = request.getDetailAddress();
		this.partyDate = partyDate;
		this.closeDate = partyDate.minusMinutes(15);
		this.createdAt = LocalDateTime.now();
	}

	//제목, 상세내용, 카테고리, 주소만 변경 가능 /현재 모임시작시간 & 모임마감시간 & 모집인원 변경 불가능
	public void update(UpdatePartyPostRequest request) {
		this.title = request.getTitle();
		this.content = request.getContent();
		this.eubMyeonDong = request.getEubMyeonDong();
		this.address = request.getAddress();
		this.detailAddress = request.getDetailAddress();
	}

	// 메소드
	// 조회수 증가
	public void increaseViewCnt(User user) {
		if (!this.user.equals(user)) {
			this.viewCnt += 1;
		}
	}

	//모집마감전인지 확인
	public boolean beforeCloseDate(LocalDateTime now) {
		return this.closeDate.isAfter(now);
	}

	//참가신청한 모집자가 없는지 확인
	public boolean haveNoApplications() {
		return this.applications.isEmpty();
	}

	//모집글 삭제(DB에서 삭제하지 않고, active 값으로 관리합니다)
	public void deletePartyPost() {
		this.active = false;
	}

	//작성자확인
	public boolean isWrittenByMe(Long userId) {
		return Objects.equals(this.user.getId(), userId);
	}

	// 들어온 참가신청을 applications 에 추가
	public void addApplication(Application application) {
		this.applications.add(application);
		checkMemberIsFull();
	}

	// 이미 참가신청한 유저인지 확인
	public boolean isAlreadyApplied() {
		return false;
	}

	//모집상태(FINDING)인지 확인
	public boolean isFinding() {
		return this.status == Status.FINDING;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// (applications 의 인원+1)과 maxMember 가 일치하는 경우, status 를 FOUND 로 변경
	private void checkMemberIsFull() {
		int acceptedMembers = this.applications.size();
		if (this.maxMember == acceptedMembers + 1) {
			this.status = Status.FOUND;
		}
	}

}
// TODO: API 1차 작업완료 후
// 차단한 유저의 게시물 블라인드 처리 방식 생각


