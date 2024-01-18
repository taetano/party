package com.example.party.repository;

import java.util.List;
import java.util.Optional;

import com.example.party.entity.Category;

public interface CategoryRepository {
	List<Category> findAllByActiveIsTrue();

	Optional<Category> findById(Long Id);

	boolean existsCategoryByName(String name);
	Category save(Category category);
}
