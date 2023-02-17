package com.example.party.category.service;

import com.example.party.category.dto.CategoryRequest;
import com.example.party.category.dto.CategoryResponse;
import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;
import com.example.party.global.dto.ResponseDto;

public interface ICategoryService {

	//카테고리 생성
	DataResponseDto<CategoryResponse> createCategory(CategoryRequest request);

	//카테고리 조회
	ListResponseDto<CategoryResponse> getCategory();

	//카테고리 수정
	DataResponseDto<CategoryResponse> updateCategory(Long categoryId, CategoryRequest request);

	//카테고리 삭제
	ResponseDto deleteCategory(Long CategoryId);
}
