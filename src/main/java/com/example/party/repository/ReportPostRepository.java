package com.example.party.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.entity.ReportPost;

public interface ReportPostRepository extends JpaRepository<ReportPost, Long> {
	Boolean existsByUserIdAndPartyPostId(Long userId, Long reportPostId);

	//모집글 신고 로그 조회
	List<ReportPost> findAllByOrderById(Pageable pageable);

	List<ReportPost> findAllByPartyPostId(Long partyPostId);
}
