package com.example.party.restrictions.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.restrictions.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
