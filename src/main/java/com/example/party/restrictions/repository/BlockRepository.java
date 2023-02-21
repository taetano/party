package com.example.party.restrictions.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.restrictions.entity.Block;
import com.example.party.user.entity.User;

public interface BlockRepository extends JpaRepository<Block, Long> {
	Boolean existsBlockerAndBlocked(User blocker, User blocked);

	Optional<Block> findByBlockerAndBlocked(User blocker, User blocked);

	List<Block> findByBlocker(User blocker, Pageable pageable);
}
