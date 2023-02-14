package com.example.party.partypost.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.partypost.entity.PartyPost;
import com.example.party.user.entity.Profile;

public interface PartyPostRepository extends JpaRepository<PartyPost, Long> {

}
