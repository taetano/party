package com.example.party.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.party.dto.request.CategoryRequest;
import com.example.party.dto.response.CategoryResponse;
import com.example.party.entity.Category;
import com.example.party.exception.CategoryNotFoundException;
import com.example.party.exception.DuplicateNameNotAllowException;
import com.example.party.repository.CategoryRepository;
import com.example.party.common.ApiResponse;
import com.example.party.common.DataApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor

public class CategoryService implements ICategoryService {

	private final CategoryRepository categoryRepository;

	//카테고리 생성
	@Override
	public ApiResponse createCategory(CategoryRequest request) {
		Category category = new Category(request);

		if (categoryRepository.existsCategoryByName(category.getName())) {
			throw new DuplicateNameNotAllowException();
		}

		categoryRepository.save(category);
		return ApiResponse.create("카테고리 생성 완료");
	}

	//카테고리 전체 조회
	@Override
	public DataApiResponse<CategoryResponse> getCategories() {
		List<Category> categoryList = categoryRepository.findAllByActiveIsTrue();

		List<CategoryResponse> allCategoryList = categoryList.stream()
			.map(CategoryResponse::new)
			.collect(Collectors.toList());

		return DataApiResponse.ok("카테고리 조회 완료", allCategoryList);
	}

	//카테고리 수정
	@Override
	public ApiResponse updateCategory(Long categoryId, CategoryRequest request) {
		Category category = getCategory(categoryId);

		if (categoryRepository.existsCategoryByName(request.getName())) {
			throw new DuplicateNameNotAllowException();
		}

		category.update(request);

		return ApiResponse.ok("카테고리 수정 완료");
	}

	//카테고리 삭제
	@Override
	public ApiResponse deleteCategory(Long categoryId) {
		Category category = getCategory(categoryId);
		category.deleteCategory(); // destroy
		return ApiResponse.ok("카테고리 삭제 완료");
	}

	@Transactional(readOnly = true)
	public Category getCategory(Long categoryId) {
		return categoryRepository.findById(categoryId).orElseThrow(
			CategoryNotFoundException::new
		);
	}
}
