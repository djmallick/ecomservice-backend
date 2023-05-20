package com.vrs.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ProductDto {
	
	private int productId;
	
	@Size(max=50,min=10, message ="Maximum length should be 50 and minimum length should be 10")
	@NotNull
	private String productName;
	
	@Size(max=1000,min=10, message ="Maximum length should be 1000 and minimum length should be 10")
	@NotNull
	private String productDescription;
	private Date productAddedDate;
	private boolean active;
	
	@Positive
	@NotNull
	private double price;
	@Positive
	@NotNull
	private int stock;
	
	private SellerDto sellerDto;
	private CategoryDto categoryDto;	
	private double averageRating;
	private List<OrderDto> orderDtos = new ArrayList<>();
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public Date getProductAddedDate() {
		return productAddedDate;
	}
	public void setProductAddedDate(Date productAddedDate) {
		this.productAddedDate = productAddedDate;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public SellerDto getSellerDto() {
		return sellerDto;
	}
	public void setSellerDto(SellerDto sellerDto) {
		this.sellerDto = sellerDto;
	}
	public CategoryDto getCategoryDto() {
		return categoryDto;
	}
	public void setCategoryDto(CategoryDto categoryDto) {
		this.categoryDto = categoryDto;
	}
	public double getAverageRating() {
		return averageRating;
	}
	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}
	public List<OrderDto> getOrderDtos() {
		return orderDtos;
	}
	public void setOrderDtos(List<OrderDto> orderDtos) {
		this.orderDtos = orderDtos;
	}
	
}
