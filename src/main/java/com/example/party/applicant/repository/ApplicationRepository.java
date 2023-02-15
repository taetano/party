package com.example.party.applicant.repository;

import com.example.party.applicant.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

  Page<Application> findAllByPartyPostAndCancelIsFalse(Long partPostId, Pageable pageable);

  boolean existsByPartyPost_partyPostIdAndUser_userId(Long partyPostId, Long userId);
}
