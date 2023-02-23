package com.example.party.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	Boolean existsUserByEmail(String email); //* 요거로 바꾸는게 어떨까요?

	Boolean existsUserByNickname(String nickname);

	List<User> findAllByNicknameIn(List<String> nicknames);
}
