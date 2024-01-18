package com.example.party.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.entity.Application;
import com.example.party.entity.PartyPost;
import com.example.party.entity.User;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

	Page<Application> findAllByPartyPostAndCancelIsFalse(PartyPost partyPost, Pageable pageable);

	boolean existsByPartyPostAndUser(PartyPost partyPost, User user);

	List<Application> findByUserId(Long userId, Pageable pageable);

	List<Application> findAllByPartyPostId(Long partyPostId);
}
