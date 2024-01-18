package com.example.party.repository;

import java.util.List;

import com.example.party.entity.Block;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block, Long> {

	List<Block> findAllByBlockerId(Long blockerId);

	List<Block> findAllByBlockerId(Long blockerId, Pageable pageable);
}

