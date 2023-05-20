package com.vrs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vrs.entities.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {

}
