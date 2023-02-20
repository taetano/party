package com.example.party.partypost.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.partypost.entity.Party;
import com.example.party.user.entity.User;

public interface PartyRepository extends JpaRepository<Party, Long> {
	Optional<Party> findByUsers(List<User> users);
}
