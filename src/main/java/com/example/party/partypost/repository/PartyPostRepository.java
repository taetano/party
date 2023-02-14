package com.example.party.partypost.repository;

import com.example.party.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.partypost.entity.PartyPost;

public interface PartyPostRepository extends JpaRepository<PartyPost, Long> {
  List<PartyPost> findByUserId(User user, Pageable pageable);
}
