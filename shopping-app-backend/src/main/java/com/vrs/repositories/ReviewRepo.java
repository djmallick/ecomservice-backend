package com.vrs.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vrs.entities.Order;
import com.vrs.entities.Product;
import com.vrs.entities.Review;

public interface ReviewRepo extends JpaRepository<Review, Integer> {
	
	Optional<Review> findByOrder(Order order);
	
	@Query(value="SELECT AVG(r.rating) from Review r, Order o where r.order.orderId=o.orderId and o.product =:product and r.rating>0")
	Double findAverageRatingByProduct(@Param("product") Product product);

}
