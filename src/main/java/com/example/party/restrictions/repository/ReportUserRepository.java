package com.example.party.restrictions.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.restrictions.entity.UserReport;
import com.example.party.user.entity.User;

public interface ReportUserRepository extends JpaRepository<UserReport, Long> {
	Boolean existsReporterAndReported(User reporter, User reported);
}
