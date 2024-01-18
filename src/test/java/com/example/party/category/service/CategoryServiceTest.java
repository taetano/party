package com.example.party.category.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import com.example.party.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.party.dto.request.CategoryRequest;
import com.example.party.dto.response.CategoryResponse;
import com.example.party.entity.Category;
import com.example.party.exception.CategoryNotFoundException;
import com.example.party.exception.DuplicateNameNotAllowException;
import com.example.party.repository.CategoryRepository;
import com.example.party.common.ApiResponse;
import com.example.party.common.DataApiResponse;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
	@Mock
	private CategoryRepository jpaCategoryRepository;

	@InjectMocks
	private CategoryService categoryService;

	private Category category;
	private CategoryRequest categoryRequest;

	@BeforeEach
	public void setUp() {
		this.category = mock(Category.class);
		this.categoryRequest = mock(CategoryRequest.class);
	}

	@Test
	void createCategory() {
		//  given
		//  when
		when(categoryRequest.getName()).thenReturn("TEST");
		when(jpaCategoryRepository.existsCategoryByName(anyString())).thenReturn(false);

		ApiResponse result = categoryService.createCategory(categoryRequest);
		//  then
		verify(jpaCategoryRepository).existsCategoryByName(anyString());
		verify(jpaCategoryRepository).save(any(Category.class));
		assertThat(result.getCode()).isEqualTo(201);
		assertThat(result.getMsg()).isEqualTo("카테고리 생성 완료");
	}

	@Test
	void getCategory() {
		//  given
		//  when
		when(jpaCategoryRepository.findAllByActiveIsTrue()).thenReturn(Collections.emptyList());

		DataApiResponse<CategoryResponse> result = categoryService.getCategories();
		//  then
		verify(jpaCategoryRepository).findAllByActiveIsTrue();
		assertThat(result.getCode()).isEqualTo(200);
		assertThat(result.getMsg()).isEqualTo("카테고리 조회 완료");
		assertThat(result.getData()).isEmpty();
	}

	@Test
	void updateCategory() {
		//  given
		//  when
		when(jpaCategoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
		when(categoryRequest.getName()).thenReturn("TEST");
		when(jpaCategoryRepository.existsCategoryByName(anyString())).thenReturn(false);

		ApiResponse result = categoryService.updateCategory(category.getId(), categoryRequest);
		//  then
		verify(jpaCategoryRepository).findById(anyLong());
		verify(jpaCategoryRepository).existsCategoryByName(anyString());
		verify(category).update(categoryRequest);
		assertThat(result.getCode()).isEqualTo(200);
		assertThat(result.getMsg()).isEqualTo("카테고리 수정 완료");
	}

	@Test
	void deleteCategory() {
		//  given
		//  when
		when(jpaCategoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

		ApiResponse result = categoryService.deleteCategory(category.getId());
		//  then
		verify(jpaCategoryRepository).findById(anyLong());
		verify(category).deleteCategory();
		assertThat(result.getCode()).isEqualTo(200);
		assertThat(result.getMsg()).isEqualTo("카테고리 삭제 완료");
	}

	// Throw Exception

	@Test
	void createCategory_DuplicateNameNotAllowException() {
		//  given
		//  when
		when(categoryRequest.getName()).thenReturn("TEST");
		when(jpaCategoryRepository.existsCategoryByName(anyString())).thenReturn(true);
		var thrown = assertThatThrownBy(
			() -> categoryService.createCategory(categoryRequest));
		//  then
		verify(jpaCategoryRepository).existsCategoryByName(anyString());
		thrown.isInstanceOf(DuplicateNameNotAllowException.class)
			.hasMessageContaining(DuplicateNameNotAllowException.MSG);
	}

	@Test
	void updateCategory_DuplicateNameNotAllowException() {
		//  given

		//  when
		when(jpaCategoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
		when(categoryRequest.getName()).thenReturn("TEST");
		when(jpaCategoryRepository.existsCategoryByName(anyString())).thenReturn(true);
		var thrown = assertThatThrownBy(
			() -> categoryService.updateCategory(category.getId(), categoryRequest));
		//  then
		verify(jpaCategoryRepository).findById(anyLong());
		verify(jpaCategoryRepository).existsCategoryByName(anyString());
		thrown.isInstanceOf(DuplicateNameNotAllowException.class)
			.hasMessageContaining(DuplicateNameNotAllowException.MSG);
	}

	@Test
	void getCategory_CategoryNotFoundException() {
		//  given

		//  when
		var  thrown = assertThatThrownBy(
			() -> categoryService.getCategory(category.getId()));
		//  then
		thrown.isInstanceOf(CategoryNotFoundException.class)
			.hasMessage(CategoryNotFoundException.MSG);
	}
}