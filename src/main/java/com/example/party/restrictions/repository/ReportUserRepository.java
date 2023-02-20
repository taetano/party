package com.example.party.restrictions.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.restrictions.entity.UserReport;

public interface ReportUserRepository extends JpaRepository<UserReport, Long> {
}
