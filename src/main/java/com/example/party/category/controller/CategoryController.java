package com.example.party.category.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.party.category.dto.CategoryRequest;
import com.example.party.category.dto.CategoryResponse;
import com.example.party.category.service.CategoryService;
import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.partypost.dto.PartyPostListResponse;
import com.example.party.partypost.service.PartyPostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

	private final CategoryService categoryService;
	private final PartyPostService partyPostService;

	//카테고리 작성
	@PostMapping("")
	public ResponseEntity<DataResponseDto<CategoryResponse>> createCategory(
		@RequestBody CategoryRequest request) {
		return ResponseEntity.ok(categoryService.createCategory(request));
	}

	//카테고리 조회
	@GetMapping("")
	public ResponseEntity<ListResponseDto<CategoryResponse>> getCategory() {
		return ResponseEntity.ok(categoryService.getCategory());
	}

	//카테고리명 별로 모집글 조회
	@GetMapping("/{categoryId}")
	public ResponseEntity<ListResponseDto<PartyPostListResponse>> searchPartyPostByCategory(
		@PathVariable Long categoryId, @RequestParam int page) {
		return ResponseEntity.ok(partyPostService.searchPartyPostByCategory(categoryId, page));
	}

	//카테고리 수정
	@PatchMapping("/{categoryId}")
	public ResponseEntity<DataResponseDto<CategoryResponse>> updateCategory(
		@PathVariable(name = "categoryId") Long categoryId, @RequestBody CategoryRequest request) {
		return ResponseEntity.ok(categoryService.updateCategory(categoryId, request));
	}

	//카테고리 삭제
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ResponseDto> deleteCategory(@PathVariable(name = "categoryId") Long categoryId) {
		return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
	}
}
