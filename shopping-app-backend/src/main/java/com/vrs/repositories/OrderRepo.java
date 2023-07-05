package com.vrs.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vrs.entities.Order;
import com.vrs.entities.Product;

public interface OrderRepo extends JpaRepository<Order, Integer> {
	Page<Order> findByProduct(Product product, Pageable p);
}
