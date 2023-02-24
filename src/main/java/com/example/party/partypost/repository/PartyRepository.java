package com.example.party.partypost.repository;

import java.util.Optional;

import com.example.party.partypost.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
	Optional<Party> findByPartyPostId(Long partyPostId);

}
