package com.vrs.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.vrs.dto.ProductDto;
import com.vrs.entities.Category;
import com.vrs.entities.Product;
import com.vrs.entities.Seller;
import com.vrs.exception.ResourceNotFoundException;
import com.vrs.payload.ProductResponse;
import com.vrs.repositories.CategoryRepo;
import com.vrs.repositories.CustomerRepo;
import com.vrs.repositories.ProductRepo;
import com.vrs.repositories.SellerRepo;
import com.vrs.repositories.UserRepo;
import com.vrs.service.ProductService;

public class ProductServiceImpl implements ProductService {
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	SellerRepo sellerRepo;
	
	@Autowired
	CustomerRepo customerRepo;
	

	@Autowired
	CategoryRepo categoryRepo;
	
	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	ModelMapper modelMapper;
	

	@Override
	public ProductDto createProduct(ProductDto productDto, Integer sellerId, Integer categoryId) {
		Seller seller = sellerRepo.findById(sellerId).orElseThrow(()-> new ResourceNotFoundException("Seller","id", sellerId));
		Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Seller","id", categoryId));
		Product product = modelMapper.map(productDto, Product.class);
		product.setActive(false);
		product.setProductAddedDate(new Date());
		product.setCategory(category);
		product.setSeller(seller);
		product.setAverageRating(0);
		Product savedProduct = productRepo.save(product);
		return modelMapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public ProductDto updateProduct(ProductDto productDto, Integer productId) {
		Product product = productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Seller","id", productId));
		product.setPrice(productDto.getPrice());
		product.setProductDescription(productDto.getProductDescription());
		product.setStock(productDto.getStock());
		product.setProductName(productDto.getProductName());
		Product updatedProduct = productRepo.save(product);
		return modelMapper.map(updatedProduct, ProductDto.class);
	}

	@Override
	public ProductDto getProductByProductId(Integer productId) {
		Product product = productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Seller","id", productId));
		return modelMapper.map(product, ProductDto.class);
	}

	@Override
	public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		Sort sort = null;
		if(sortDir.equals("desc")) {
			sort = Sort.by(sortBy).descending();
		}
		else {
			sort = Sort.by(sortBy).ascending();
		}
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> pageProducts = productRepo.findAll(p);
		List<Product> products = pageProducts.getContent();
		List<ProductDto> fetchedProducts = products.stream().map(product->modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
		ProductResponse productResponse = new ProductResponse();
		productResponse.setContents(fetchedProducts);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setLastPage(pageProducts.isLast());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		return productResponse;
	}

	@Override
	public void deleteProduct(Integer productId) {
		Product product = productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Seller","id", productId));
		productRepo.delete(product);
	}

	@Override
	public ProductResponse getProductsBySellerId(Integer sellerId, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir) {
		Seller seller = sellerRepo.findById(sellerId).orElseThrow(()-> new ResourceNotFoundException("Seller","id", sellerId));
		Sort sort = null;
		if(sortDir.equals("desc")) {
			sort = Sort.by(sortBy).descending();
		}
		else {
			sort = Sort.by(sortBy).ascending();
		}
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> pageProducts = productRepo.findBySeller(seller, p);
		List<Product> products = pageProducts.getContent();
		List<ProductDto> fetchedProducts = products.stream().map(product->modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
		ProductResponse productResponse = new ProductResponse();
		productResponse.setContents(fetchedProducts);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setLastPage(pageProducts.isLast());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		return productResponse;
	}

	@Override
	public ProductResponse getProductsByCategoryId(Integer categoryId, Integer pageNumber, Integer pageSize,
			String sortBy, String sortDir) {
		Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Seller","id", categoryId));

		Sort sort = null;
		if(sortDir.equals("desc")) {
			sort = Sort.by(sortBy).descending();
		}
		else {
			sort = Sort.by(sortBy).ascending();
		}
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> pageProducts = productRepo.findByCategory(category, p);
		List<Product> products = pageProducts.getContent();
		List<ProductDto> fetchedProducts = products.stream().map(product->modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
		ProductResponse productResponse = new ProductResponse();
		productResponse.setContents(fetchedProducts);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setLastPage(pageProducts.isLast());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		return productResponse;
	}



}
