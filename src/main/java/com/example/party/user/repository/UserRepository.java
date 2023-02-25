package com.example.party.user.repository;

import java.util.List;
import java.util.Optional;

import com.example.party.partypost.entity.PartyPost;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.user.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Boolean existsUserByEmail(String email);

	Boolean existsUserByNickname(String nickname);

	List<User> findAllByNicknameIn(List<String> nicknames);

	@Modifying
	@Query(value = "SELECT u FROM User u WHERE u.status = 'SUSPENDED'")
	List<User> statusEqualSuspended();
}
