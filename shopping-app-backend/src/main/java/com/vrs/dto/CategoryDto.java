package com.vrs.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

public class CategoryDto {
	
	private Integer categoryId;

	@NotEmpty(message = "Must not be empty")
	private String categoryTitle;

	@NotEmpty(message = "Must not be empty")
	private String categoryDescription;
	
	private List<ProductDto> productDtos = new ArrayList<>();

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryTitle() {
		return categoryTitle;
	}

	public void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public List<ProductDto> getProductDtos() {
		return productDtos;
	}

	public void setProductDtos(List<ProductDto> productDtos) {
		this.productDtos = productDtos;
	}

}
