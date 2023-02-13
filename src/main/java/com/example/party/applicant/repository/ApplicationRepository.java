package com.example.party.applicant.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.applicant.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
	Page<Application> findAllByPartyPostAndCancelIsFalse(Long partPostId, Pageable pageable);
}
