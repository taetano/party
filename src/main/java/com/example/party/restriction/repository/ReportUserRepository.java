package com.example.party.restriction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.restriction.entity.ReportUser;

public interface ReportUserRepository extends JpaRepository<ReportUser, Long> {
	Boolean existsByReporterIdAndReportedId(Long reporterId, Long reportedId);
}
