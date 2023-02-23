package com.example.party.restriction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.restriction.entity.ReportPost;

public interface ReportPostRepository extends JpaRepository<ReportPost, Long> {
	Boolean existsByUserIdAndReportPostId(Long userId, Long reportPostId);
}
