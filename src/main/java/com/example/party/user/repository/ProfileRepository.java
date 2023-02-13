package com.example.party.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.user.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	boolean existsProfileBy(String profile);

}
