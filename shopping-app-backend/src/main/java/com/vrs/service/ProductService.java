package com.vrs.service;

import com.vrs.dto.ProductDto;
import com.vrs.payload.ProductResponse;

public interface ProductService {
	
	ProductDto createProduct(ProductDto productDto, Integer sellerId, Integer categoryId);
	
	ProductDto updateProduct(ProductDto productDto, Integer productId);
	
	ProductDto getProductByProductId(Integer productId);
	
	ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	
	boolean deleteProduct(Integer productId);
	
	ProductResponse getProductsBySellerId(Integer sellerId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	
	ProductResponse getProductsByCategoryId(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	
	ProductResponse searchProducts(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir);
}
