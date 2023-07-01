package com.vrs.service;

import java.util.List;

import com.vrs.dto.CategoryDto;

public interface CategoryService {
	CategoryDto createCategory(CategoryDto categoryDto);
	CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId);
	List<CategoryDto> getAllCategories();
	CategoryDto getCategoryById(Integer categoryId);
	boolean deleteCategory(Integer categoryId);
}
