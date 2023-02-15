package com.example.party.application.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.application.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

	Page<Application> findAllByPartyPostAndCancelIsFalse(Long partPostId, Pageable pageable);

	boolean existsByPartyPost_partyPostIdAndUser_userId(Long partyPostId, Long userId);

	List<Application> findByUserId(Long userId, Pageable pageable);
}
