package com.example.party.category.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.party.category.dto.CategoryRequest;
import com.example.party.category.dto.CategoryResponse;
import com.example.party.category.entity.Category;
import com.example.party.category.exception.CategoryNotFoundException;
import com.example.party.category.exception.DuplicateNameNotAllowException;
import com.example.party.category.repository.CategoryRepository;
import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;
import com.example.party.global.dto.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor

public class CategoryService implements ICategoryService{

	private final CategoryRepository categoryRepository;

	//카테고리 생성
	@Override
	public DataResponseDto<CategoryResponse> createCategory(CategoryRequest request) {
		Category category = new Category(request);

		Optional<Category> found = categoryRepository.findByName(category.getName());
		if(found.isPresent()) {
			throw new DuplicateNameNotAllowException();
		}

		categoryRepository.save(category);
		CategoryResponse categoryResponse = new CategoryResponse(category);
		return new DataResponseDto<>(201, "카테고리 생성 완료", categoryResponse);
	}

	//카테고리 조회
	@Override
	public ListResponseDto<CategoryResponse> getCategory() {
		List<Category> categoryList = categoryRepository.findAll();
		List<CategoryResponse> allCategoryList = categoryList.stream().map(
			category -> new CategoryResponse(category)).collect(Collectors.toList());
		return ListResponseDto.ok("카테고리 조회 완료", allCategoryList);
	}

	//카테고리 수정
	@Override
	public DataResponseDto<CategoryResponse> updateCategory(Long categoryId, CategoryRequest request) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(
			() -> new CategoryNotFoundException()
		);

		Optional<Category> found = categoryRepository.findByName(category.getName());
		if(found.isPresent()) {
			throw new DuplicateNameNotAllowException();
		}

		category.update(request);

		CategoryResponse categoryResponse = new CategoryResponse(category);
		return DataResponseDto.ok("카테고리 수정 완료", categoryResponse);
	}

	//카테고리 삭제
	@Override
	public ResponseDto deleteCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(
			() -> new CategoryNotFoundException()
		);

		categoryRepository.deleteById(category.getId());

		return ResponseDto.ok("카테고리 삭제 완료");
	}
}
