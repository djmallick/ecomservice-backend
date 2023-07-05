package com.vrs.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vrs.entities.Customer;
import com.vrs.entities.Order;
import com.vrs.entities.Product;

public interface OrderRepo extends JpaRepository<Order, Integer> {
//	@Query("from Order O where O.active like :key")
	Page<Order> findByProduct(Product product, Pageable p);
	Page<Order> findByCustomer(Customer customer, Pageable p);
}
