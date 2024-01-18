package com.example.party.service;

import com.example.party.dto.request.CategoryRequest;
import com.example.party.dto.response.CategoryResponse;
import com.example.party.common.ApiResponse;
import com.example.party.common.DataApiResponse;

public interface ICategoryService {

	//카테고리 생성
	ApiResponse createCategory(CategoryRequest request);

	//카테고리 조회
	DataApiResponse<CategoryResponse> getCategories();

	//카테고리 수정
	ApiResponse updateCategory(Long categoryId, CategoryRequest request);

	//카테고리 삭제
	ApiResponse deleteCategory(Long CategoryId);
}
