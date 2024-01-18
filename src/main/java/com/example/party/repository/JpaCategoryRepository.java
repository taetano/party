package com.example.party.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.party.entity.Category;

public interface JpaCategoryRepository extends JpaRepository<Category, Long>, CategoryRepository{

	//카테고리 조회
	List<Category> findAllByActiveIsTrue();

	Optional<Category> findById(Long Id);

	boolean existsCategoryByName(String name);

}
