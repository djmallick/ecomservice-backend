package com.vrs.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vrs.entities.Category;
import com.vrs.entities.Product;
import com.vrs.entities.Seller;

public interface ProductRepo extends JpaRepository<Product, Integer> {
	Page<Product> findBySeller(Seller seller, Pageable p);
	Page<Product> findByCategory(Category category, Pageable p);
	Page<Product> findByProductNameContaining(String keyword, Pageable p);
}
