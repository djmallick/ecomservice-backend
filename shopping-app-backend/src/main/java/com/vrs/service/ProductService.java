package com.vrs.service;

import java.util.List;

import com.vrs.dto.ProductDto;

public interface ProductService {
	
	ProductDto createProduct(ProductDto productDto);
	
	ProductDto updateProduct(ProductDto productDto, Integer productId);
	
	ProductDto getProductByProductId(Integer productId);
	
	List<ProductDto> getAllProducts();
	
	void deleteProduct(Integer productId);
	
	List<ProductDto> getProductsBySellerId(Integer sellerId);
}
