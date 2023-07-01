package com.vrs.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vrs.dto.CategoryDto;
import com.vrs.payload.ApiResponse;
import com.vrs.service.CategoryService;

@RestController
@RequestMapping("/api")
public class CategoryController {

	@Autowired
	CategoryService categoryService;
	
	@PostMapping("/category")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
		CategoryDto createdCategory = categoryService.createCategory(categoryDto);
		return new ResponseEntity<CategoryDto>(createdCategory, HttpStatus.CREATED);	
	}
	
	@PutMapping("/category/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Integer categoryId) {
		CategoryDto updatedCategory = categoryService.updateCategory(categoryDto, categoryId);
		return new ResponseEntity<CategoryDto>(updatedCategory, HttpStatus.ACCEPTED);	
	}
	
	@DeleteMapping("/category/{categoryId}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId) {
		boolean isDeleted = categoryService.deleteCategory(categoryId);
		if(isDeleted) {
			return new ResponseEntity<ApiResponse>(new ApiResponse("Category deleted successfully!",true), HttpStatus.OK);	
		}
		return new ResponseEntity<ApiResponse>(new ApiResponse("There is an issue to delete the product",false), HttpStatus.NOT_IMPLEMENTED);	
	}
	
	@GetMapping("/category/{categoryId}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer categoryId) {
		CategoryDto fetchedCategory = categoryService.getCategoryById(categoryId);
		return new ResponseEntity<CategoryDto>(fetchedCategory, HttpStatus.OK);	
	}
	
	@GetMapping("/category")
	public ResponseEntity<List<CategoryDto>> getAllCategories() {
		List<CategoryDto> fetchedCategories = categoryService.getAllCategories();
		return new ResponseEntity<List<CategoryDto>>(fetchedCategories, HttpStatus.OK);	
	}
	
}
