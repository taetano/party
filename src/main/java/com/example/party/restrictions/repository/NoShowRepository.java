package com.example.party.restrictions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.restrictions.entity.NoShow;

public interface NoShowRepository extends JpaRepository<NoShow, Long> {
	Boolean existsByReporterIdAndPostIdAndReportedId(Long userId, Long postId, Long reportedId);

	// Optional<NoShow> findByPostIdAndReportedId(Long postId, Long reportedId);

	List<NoShow> findAllByPostId(Long postId);
}
