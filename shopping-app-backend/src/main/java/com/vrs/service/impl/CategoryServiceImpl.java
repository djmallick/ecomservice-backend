package com.vrs.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vrs.dto.CategoryDto;
import com.vrs.repositories.CategoryRepo;
import com.vrs.service.CategoryService;
import com.vrs.entities.Category;
import com.vrs.exception.ResourceNotFoundException;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	CategoryRepo categoryRepo;
	
	
	@Autowired
	ModelMapper modelMapper;
	

	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		Category savedCategory = categoryRepo.save(modelMapper.map(categoryDto, Category.class));
		return categoryToCategoryDto(savedCategory);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
		Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","id", categoryId));
		category.setCategoryDescription(categoryDto.getCategoryDescription());
		category.setCategoryTitle(categoryDto.getCategoryTitle());
		Category updatedCategory = categoryRepo.save(category);	
		return categoryToCategoryDto(updatedCategory);
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		List<Category> categories = categoryRepo.findAll();
		List<CategoryDto> categoryDtos = categories.stream().map(category->categoryToCategoryDto(category)).collect(Collectors.toList());
		return categoryDtos;
	}

	@Override
	public CategoryDto getCategoryById(Integer categoryId) {
		Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","id", categoryId));
		return categoryToCategoryDto(category);
	}

	@Override
	public boolean deleteCategory(Integer categoryId) {
		Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","id", categoryId));
		categoryRepo.delete(category);
		Optional<Category> deletedCategory = categoryRepo.findById(categoryId);
		return deletedCategory.isEmpty();
	}
	
	private CategoryDto categoryToCategoryDto(Category category) {
		return this.modelMapper.map(category, CategoryDto.class);
	}


}
