package com.example.party.applicant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.applicant.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
	List<Application> findAllByPartyPostAndCancelIsFalse(Long partPostId);
}
