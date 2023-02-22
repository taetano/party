package com.example.party.restrictions.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.restrictions.entity.Blocks;

public interface BlockRepository extends JpaRepository<Blocks, Long> {

	Optional<Blocks> findByBlockerId(Long blockerId);

	List<Blocks> findAllByBlockerId(Long blockerId);

	List<Blocks> findAllByBlockerId(Long blockerId, Pageable pageable);
}

