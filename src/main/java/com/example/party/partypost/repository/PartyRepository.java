package com.example.party.partypost.repository;

import java.util.Optional;

import com.example.party.partypost.entity.Parties;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Parties, Long> {
	Optional<Parties> findByPartyPostId(Long partyPostId);

}
