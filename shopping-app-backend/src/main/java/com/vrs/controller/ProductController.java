package com.vrs.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vrs.config.AppConstants;
import com.vrs.dto.ProductDto;
import com.vrs.payload.ApiResponse;
import com.vrs.payload.ProductResponse;
import com.vrs.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@PostMapping("/product/seller/{sellerId}/category/{categoryId}")
	public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto, 
			@PathVariable Integer sellerId, @PathVariable Integer categoryId) {
		ProductDto createdProduct = productService.createProduct(productDto, sellerId, categoryId);
		return new ResponseEntity<ProductDto>(createdProduct, HttpStatus.CREATED);	
	}
	
	@PutMapping("/product/{productId}")
	public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto,@PathVariable Integer productId) {
		ProductDto updatedProduct = productService.updateProduct(productDto, productId);
		return new ResponseEntity<ProductDto>(updatedProduct, HttpStatus.OK);	
	}
	
	@PutMapping("/product/activate/{productId}")
	public ResponseEntity<ProductDto> activateProduct(@PathVariable Integer productId) {
		ProductDto updatedProduct = productService.activateProduct(productId, true);
		return new ResponseEntity<ProductDto>(updatedProduct, HttpStatus.OK);	
	}
	
	@PutMapping("/product/deactivate/{productId}")
	public ResponseEntity<ProductDto> deactivateProduct(@PathVariable Integer productId) {
		ProductDto updatedProduct = productService.activateProduct(productId, false);
		return new ResponseEntity<ProductDto>(updatedProduct, HttpStatus.OK);	
	}
	
	@DeleteMapping("/product/{productId}")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Integer productId) {
		boolean isDeleted = productService.deleteProduct(productId);
		if(isDeleted) {
			return new ResponseEntity<ApiResponse>(new ApiResponse("Product deleted successfully!",true), HttpStatus.OK);	
		}
		return new ResponseEntity<ApiResponse>(new ApiResponse("There is an issue to delete the product",false), HttpStatus.NOT_IMPLEMENTED);	
	}
	
	@GetMapping("/product")
	public ResponseEntity<ProductResponse> getAllProducts(
			@RequestParam(value="pageNumber", defaultValue=AppConstants.PAGE_NUMBER, required =false) Integer pageNumber,
			@RequestParam(value="pageSize", defaultValue=AppConstants.PAGE_SIZE, required =false) Integer pageSize,
			@RequestParam(value="sortBy", defaultValue=AppConstants.SORT_BY, required=false) String sortBy,
			@RequestParam(value="sortDir", defaultValue=AppConstants.SORT_DIR, required=false) String sortDir
			) {
				ProductResponse allProducts = productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
				return new ResponseEntity<ProductResponse>(allProducts, HttpStatus.OK);	
	}
	
	
	@GetMapping("/seller/{sellerId}/product")
	public ResponseEntity<ProductResponse> getProductsBySeller(
			@PathVariable Integer sellerId,
			@RequestParam(value="pageNumber", defaultValue=AppConstants.PAGE_NUMBER, required =false) Integer pageNumber,
			@RequestParam(value="pageSize", defaultValue=AppConstants.PAGE_SIZE, required =false) Integer pageSize,
			@RequestParam(value="sortBy", defaultValue=AppConstants.SORT_BY, required=false) String sortBy,
			@RequestParam(value="sortDir", defaultValue=AppConstants.SORT_DIR, required=false) String sortDir
			) {
				ProductResponse allProducts = productService.getProductsBySellerId(sellerId, pageNumber, pageSize, sortBy, sortDir);
				return new ResponseEntity<ProductResponse>(allProducts, HttpStatus.OK);	
	}

	@GetMapping("/category/{categoryId}/product")
	public ResponseEntity<ProductResponse> getProductsByCategory(
			@PathVariable Integer categoryId,
			@RequestParam(value="pageNumber", defaultValue=AppConstants.PAGE_NUMBER, required =false) Integer pageNumber,
			@RequestParam(value="pageSize", defaultValue=AppConstants.PAGE_SIZE, required =false) Integer pageSize,
			@RequestParam(value="sortBy", defaultValue=AppConstants.SORT_BY, required=false) String sortBy,
			@RequestParam(value="sortDir", defaultValue=AppConstants.SORT_DIR, required=false) String sortDir
			) {
				ProductResponse allProducts = productService.getProductsByCategoryId(categoryId, pageNumber, pageSize, sortBy, sortDir);
				return new ResponseEntity<ProductResponse>(allProducts, HttpStatus.OK);	
	}
	
	@GetMapping("/product/{productId}")
	public ResponseEntity<ProductDto> getProductsById(@PathVariable Integer productId) {
		ProductDto fetchedProduct = productService.getProductByProductId(productId);
		return new ResponseEntity<ProductDto>(fetchedProduct, HttpStatus.OK);	
	}
	
	@GetMapping("/product/search")
	public ResponseEntity<ProductResponse> searchProductByName(
			@RequestParam String keyword,
			@RequestParam(value="pageNumber", defaultValue=AppConstants.PAGE_NUMBER, required =false) Integer pageNumber,
			@RequestParam(value="pageSize", defaultValue=AppConstants.PAGE_SIZE, required =false) Integer pageSize,
			@RequestParam(value="sortBy", defaultValue=AppConstants.SORT_BY, required=false) String sortBy,
			@RequestParam(value="sortDir", defaultValue=AppConstants.SORT_DIR, required=false) String sortDir) {
		ProductResponse fetchedProduct = productService.searchProducts(keyword, pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<ProductResponse>(fetchedProduct, HttpStatus.OK);	
	}
}
