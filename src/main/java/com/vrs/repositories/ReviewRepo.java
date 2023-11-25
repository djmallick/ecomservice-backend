package com.vrs.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vrs.entities.Customer;
import com.vrs.entities.Order;
import com.vrs.entities.Product;
import com.vrs.entities.Review;
import com.vrs.entities.Seller;

public interface ReviewRepo extends JpaRepository<Review, Integer> {
	
	Optional<Review> findByOrder(Order order);
	
	@Query(value="SELECT AVG(r.rating) from Review r, Order o where r.order.orderId=o.orderId and o.product =:product and r.rating>0")
	Double findAverageRatingByProduct(@Param("product") Product product);

	@Query(value="SELECT r from Review r, Order o where r.order.orderId=o.orderId and o.product =:product")
	Page<Review> findByProduct(@Param("product") Product product, Pageable p);

	@Query(value="SELECT r from Review r, Order o, Product p where r.order.orderId=o.orderId and o.product.productId = p.productId and p.seller =:seller")
	Page<Review> findBySeller(@Param("seller") Seller seller, Pageable p);

	Page<Review> findByCustomer(Customer customer, Pageable p);
}
