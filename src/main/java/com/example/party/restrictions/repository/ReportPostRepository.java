package com.example.party.restrictions.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.restrictions.entity.PostReport;

public interface ReportPostRepository extends JpaRepository<PostReport, Long> {
	Optional<PostReport> findByUserIdAndReportPostId(Long userId, Long partyPostId);
}
