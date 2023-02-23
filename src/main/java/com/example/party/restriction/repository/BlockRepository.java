package com.example.party.restriction.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.restriction.entity.Blocks;

public interface BlockRepository extends JpaRepository<Blocks, Long> {

	List<Blocks> findAllByBlockerId(Long blockerId);

	List<Blocks> findAllByBlockerId(Long blockerId, Pageable pageable);
}

