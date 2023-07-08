package com.vrs.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vrs.entities.Customer;
import com.vrs.entities.Order;
import com.vrs.entities.Product;
import com.vrs.entities.Seller;

public interface OrderRepo extends JpaRepository<Order, Integer> {
	Page<Order> findByProduct(Product product, Pageable p);
	
	@Query(value="SELECT o from Order o where o.active=:active and o.product =:product")
	Page<Order> findByProductActiveOrder(@Param("product") Product product, @Param("active") boolean active, Pageable p);
	
	@Query(value="SELECT o from Order o where o.active=:active and o.customer =:customer")
	Page<Order> findByCustomerActiveOrder(@Param("customer") Customer customer, @Param("active") boolean active, Pageable p);
	
	Page<Order> findByCustomer(Customer customer, Pageable p);

	@Query(value="SELECT o from Order o, Product p where o.product.productId=p.productId and p.seller =:seller")
	Page<Order> findBySeller(@Param("seller") Seller seller, Pageable p);
	
	@Query(value="SELECT o from Order o, Product p where o.active=:active and o.product.productId=p.productId and p.seller =:seller")
	Page<Order> findBySellerActiveOrder(@Param("seller") Seller seller, @Param("active") boolean active, Pageable p);
}
