package com.example.party.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.user.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	// boolean existsProfileBy(String profile); jpa 메소드명 작성 규칙에 대해 알아봐주세요.

}
