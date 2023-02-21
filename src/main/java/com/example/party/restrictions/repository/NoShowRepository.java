package com.example.party.restrictions.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.restrictions.entity.NoShow;

public interface NoShowRepository extends JpaRepository<NoShow, Long> {
	Boolean existsReporterIdAndPostIdAndReportedId(Long userId, Long postId, Long reportedId);
}
