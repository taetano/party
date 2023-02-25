package com.example.party.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.user.entity.User;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Boolean existsUserByEmail(String email);

	Boolean existsUserByNickname(String nickname);

	List<User> findAllByNicknameIn(List<String> nicknames);

	@Query(value = "SELECT u FROM User u WHERE u.status = 'SUSPENDED'")
	List<User> statusEqualSuspended();

	@Query(value = "SELECT p FROM Profile p WHERE p.noShowCnt >= 1 ")
	List<User> findAllByNoShowList();

}
