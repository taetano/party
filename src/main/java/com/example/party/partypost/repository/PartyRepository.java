package com.example.party.partypost.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.partypost.entity.Party;

public interface PartyRepository extends JpaRepository<Party, Long> {
	Optional<Party> findByPartyPostId(Long partyPostId);
}
