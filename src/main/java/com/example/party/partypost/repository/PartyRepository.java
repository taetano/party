package com.example.party.partypost.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.partypost.entity.Partys;

public interface PartyRepository extends JpaRepository<Partys, Long> {
	Optional<Partys> findByPartyPostId(Long partyPostId);
}
