package com.vrs.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vrs.dto.CategoryDto;
import com.vrs.dto.CustomerDto;
import com.vrs.dto.ProductDto;
import com.vrs.dto.SellerDto;
import com.vrs.dto.UserDto;
import com.vrs.entities.Category;
import com.vrs.entities.Customer;
import com.vrs.entities.Product;
import com.vrs.entities.Seller;
import com.vrs.entities.User;
import com.vrs.exception.ResourceNotFoundException;
import com.vrs.payload.ProductResponse;
import com.vrs.repositories.CategoryRepo;
import com.vrs.repositories.CustomerRepo;
import com.vrs.repositories.ProductRepo;
import com.vrs.repositories.SellerRepo;
import com.vrs.repositories.UserRepo;
import com.vrs.service.ProductService;

@Service
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
		SellerDto sellerDto = modelMapper.map(savedProduct.getSeller(), SellerDto.class);
		CategoryDto categoryDto = modelMapper.map(savedProduct.getCategory(), CategoryDto.class);
		ProductDto pd = modelMapper.map(savedProduct, ProductDto.class);
		pd.setSellerDto(sellerDto);
		pd.setCategoryDto(categoryDto);
		return pd;
	}

	@Override
	public ProductDto updateProduct(ProductDto productDto, Integer productId) {
		Product product = productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Seller","id", productId));
		product.setPrice(productDto.getPrice());
		product.setProductDescription(productDto.getProductDescription());
		product.setStock(productDto.getStock());
		product.setProductName(productDto.getProductName());
		Product updatedProduct = productRepo.save(product);
		SellerDto sellerDto = modelMapper.map(updatedProduct.getSeller(), SellerDto.class);
		CategoryDto categoryDto = modelMapper.map(updatedProduct.getCategory(), CategoryDto.class);
		ProductDto pd = modelMapper.map(updatedProduct, ProductDto.class);
		pd.setSellerDto(sellerDto);
		pd.setCategoryDto(categoryDto);
		return pd;	
	}
	
	@Override
	public ProductDto activateProduct(Integer productId, boolean active) {
		Product product = productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Seller","id", productId));
		product.setActive(active);
		Product updatedProduct = productRepo.save(product);
		SellerDto sellerDto = modelMapper.map(updatedProduct.getSeller(), SellerDto.class);
		CategoryDto categoryDto = modelMapper.map(updatedProduct.getCategory(), CategoryDto.class);
		ProductDto pd = modelMapper.map(updatedProduct, ProductDto.class);
		pd.setSellerDto(sellerDto);
		pd.setCategoryDto(categoryDto);
		return pd;	
	}

	@Override
	public ProductDto getProductByProductId(Integer productId) {
		Product product = productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product","id", productId));
		SellerDto sellerDto = modelMapper.map(product.getSeller(), SellerDto.class);
		CategoryDto categoryDto = modelMapper.map(product.getCategory(), CategoryDto.class);
		ProductDto pd = modelMapper.map(product, ProductDto.class);
		pd.setSellerDto(sellerDto);
		pd.setCategoryDto(categoryDto);
		return pd;	
	}

	@Override
	public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
//		System.out.println("User email: "+SecurityContextHolder.getContext().getAuthentication().getName());
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
		List<ProductDto> fetchedProducts = new ArrayList<ProductDto>();
		for(Product product:products) {
			SellerDto sd = modelMapper.map(product.getSeller(), SellerDto.class);
			CategoryDto cd = modelMapper.map(product.getCategory(), CategoryDto.class);
			ProductDto pd = modelMapper.map(product, ProductDto.class);
			pd.setSellerDto(sd);
			pd.setCategoryDto(cd);
			fetchedProducts.add(pd);
		}
		return createProductResponse(fetchedProducts, pageProducts);
	}

	@Override
	public boolean deleteProduct(Integer productId) {
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByEmail(userEmail).get();
		
		Product product = productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product","id", productId));
		if(product.getSeller().getUser().getUserId()==user.getUserId()) {
			productRepo.delete(product);
			Optional<Product> deletedProduct = productRepo.findById(productId);
			return deletedProduct.isEmpty();
		} else {
			System.out.println("Unauthorized: User "+user.getUserId() + " tries to delete product of different user "+product.getSeller().getUser().getUserId());
			return false;
		}
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
		List<ProductDto> fetchedProducts = new ArrayList<ProductDto>();
		for(Product product:products) {
			SellerDto sd = modelMapper.map(product.getSeller(), SellerDto.class);
			CategoryDto cd = modelMapper.map(product.getCategory(), CategoryDto.class);
			ProductDto pd = modelMapper.map(product, ProductDto.class);
			pd.setSellerDto(sd);
			pd.setCategoryDto(cd);
			fetchedProducts.add(pd);
		}
		return createProductResponse(fetchedProducts, pageProducts);
	}

	@Override
	public ProductResponse getProductsByCategoryId(Integer categoryId, Integer pageNumber, Integer pageSize,
			String sortBy, String sortDir) {
		Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","id", categoryId));

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
		List<ProductDto> fetchedProducts = new ArrayList<ProductDto>();
		for(Product product:products) {
			SellerDto sd = modelMapper.map(product.getSeller(), SellerDto.class);
			CategoryDto cd = modelMapper.map(product.getCategory(), CategoryDto.class);
			ProductDto pd = modelMapper.map(product, ProductDto.class);
			pd.setSellerDto(sd);
			pd.setCategoryDto(cd);
			fetchedProducts.add(pd);
		}
		return createProductResponse(fetchedProducts, pageProducts);
	}

	@Override
	public ProductResponse searchProducts(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		Sort sort = null;
		if(sortDir.equals("desc")) {
			sort = Sort.by(sortBy).descending();
		}
		else {
			sort = Sort.by(sortBy).ascending();
		}
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<Product> pageProducts = productRepo.findByProductNameContaining(keyword, p);
		List<Product> products = pageProducts.getContent();
		List<ProductDto> fetchedProducts = new ArrayList<ProductDto>();
		for(Product product:products) {
			SellerDto sd = modelMapper.map(product.getSeller(), SellerDto.class);
			CategoryDto cd = modelMapper.map(product.getCategory(), CategoryDto.class);
			ProductDto pd = modelMapper.map(product, ProductDto.class);
			pd.setSellerDto(sd);
			pd.setCategoryDto(cd);
			fetchedProducts.add(pd);
		}
		return createProductResponse(fetchedProducts, pageProducts);
	}
	
	public static ProductResponse createProductResponse(List<ProductDto> products, Page<Product> pageProducts) {
		ProductResponse productResponse = new ProductResponse();
		productResponse.setContents(products);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setLastPage(pageProducts.isLast());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		return productResponse;
	}



}
