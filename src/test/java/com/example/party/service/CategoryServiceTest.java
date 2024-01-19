package com.example.party.service;

import com.example.party.common.ApiResponse;
import com.example.party.common.DataApiResponse;
import com.example.party.dto.request.CategoryRequest;
import com.example.party.dto.response.CategoryResponse;
import com.example.party.entity.Category;
import com.example.party.exception.CategoryNotFoundException;
import com.example.party.exception.DuplicateNameNotAllowException;
import com.example.party.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    CategoryService categoryService;

    @DisplayName("성공케이스")
    @Nested
    public class Success {
        @DisplayName("카테고리 생성")
        @Test
        void createCategory() {
            // given
            CategoryRequest mockRequest = mock(CategoryRequest.class);
            given(mockRequest.getName()).willReturn("name");
            given(categoryRepository.existsCategoryByName(anyString())).willReturn(false);
            // when
            ApiResponse result = categoryService.createCategory(mockRequest);
            //then
            then(categoryRepository).should().existsCategoryByName(anyString());
            then(categoryRepository).should().save(any(Category.class));
            assertThat(result.getCode()).isEqualTo(201);
            assertThat(result.getMsg()).isEqualTo("카테고리 생성 완료");
        }

        @DisplayName("카테고리 목록 조회")
        @Test
        void getCategories() {
            // given
            Category mockCategory = mock(Category.class);
            given(mockCategory.getId()).willReturn(1L);
            given(mockCategory.getName()).willReturn("name");
            given(categoryRepository.findAllByActiveIsTrue()).willReturn(List.of(mockCategory));
            // when
            DataApiResponse<CategoryResponse> result = categoryService.getCategories();
            //then
            then(categoryRepository).should().findAllByActiveIsTrue();
            assertThat(result.getData()).isNotEmpty();
            assertThat(result.getCode()).isEqualTo(200);
            assertThat(result.getMsg()).isEqualTo("카테고리 조회 완료");
        }

        @DisplayName("카테고리 수정")
        @Test
        void updateCategory() {
            // given
            CategoryRequest mockRequest = mock(CategoryRequest.class);
            given(mockRequest.getName()).willReturn("name");
            Category mockCategory = mock(Category.class);
            given(categoryRepository.findById(anyLong())).willReturn(Optional.of(mockCategory));
            given(categoryRepository.existsCategoryByName(anyString())).willReturn(false);
            // when
            ApiResponse result = categoryService.updateCategory(1L, mockRequest);
            //then
            then(categoryRepository).should().findById(anyLong());
            then(categoryRepository).should().existsCategoryByName(anyString());
            assertThat(result.getCode()).isEqualTo(200);
            assertThat(result.getMsg()).isEqualTo("카테고리 수정 완료");
        }

        @DisplayName("카테고리 삭제")
        @Test
        void deleteCategory() {
            // given
            Category mockCategory = mock(Category.class);
            given(categoryRepository.findById(anyLong())).willReturn(Optional.of(mockCategory));
            // when
            ApiResponse result = categoryService.deleteCategory(1L);
            //then
            then(categoryRepository).should().findById(anyLong());
            assertThat(result.getCode()).isEqualTo(200);
            assertThat(result.getMsg()).isEqualTo("카테고리 삭제 완료");
        }

        @DisplayName("카테고리 조회")
        @Test
        void getCategory() {
            // given
            Category mockCategory = mock(Category.class);
            given(categoryRepository.findById(anyLong())).willReturn(Optional.of(mockCategory));
            // when
            Category result = categoryService.getCategory(1L);
            //then
            then(categoryRepository).should().findById(anyLong());
            assertThat(result).isEqualTo(mockCategory);
        }
    }

    @DisplayName("실패케이스")
    @Nested
    public class Fail {
        @DisplayName("중복 카테고리 생성시 오류")
        @Test
        void createCategory() {
            // given
            CategoryRequest mockRequest = mock(CategoryRequest.class);
            given(mockRequest.getName()).willReturn("name");
            given(categoryRepository.existsCategoryByName(anyString())).willReturn(true);
            // when
            var thrown =
                    assertThatThrownBy(() -> categoryService.createCategory(mockRequest));
            //then
            then(categoryRepository).should().existsCategoryByName(anyString());
            thrown.isInstanceOf(DuplicateNameNotAllowException.class)
                    .hasMessage(DuplicateNameNotAllowException.MSG);
        }

        @DisplayName("카테고리 수정시 중복 카테고리로 변경시 오류")
        @Test
        void updateCategory() {
            // given
            CategoryRequest mockRequest = mock(CategoryRequest.class);
            given(mockRequest.getName()).willReturn("name");
            Category mockCategory = mock(Category.class);
            given(categoryRepository.findById(anyLong())).willReturn(Optional.of(mockCategory));
            given(categoryRepository.existsCategoryByName(anyString())).willReturn(true);
            // when
            var thrown =
                    assertThatThrownBy(() -> categoryService.updateCategory(1L, mockRequest));
            //then
            then(categoryRepository).should().findById(anyLong());
            then(categoryRepository).should().existsCategoryByName(anyString());
            thrown.isInstanceOf(DuplicateNameNotAllowException.class)
                    .hasMessage(DuplicateNameNotAllowException.MSG);
        }

        @DisplayName("카테고리 조회 실패 오류")
        @Test
        void getCategory() {
            // given
            given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());
            // when
            var thrown =
                    assertThatThrownBy(() -> categoryService.getCategory(1L));
            //then
            then(categoryRepository).should().findById(anyLong());
            thrown.isInstanceOf(CategoryNotFoundException.class)
                    .hasMessage(CategoryNotFoundException.MSG);
        }
    }
}