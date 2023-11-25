package com.vrs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vrs.entities.Customer;
import com.vrs.entities.Order;
import com.vrs.entities.OrderCancellationRequest;
import com.vrs.entities.Seller;

public interface OrderCancellationRequestRepo extends JpaRepository<OrderCancellationRequest, Integer> {
	@Query(value="SELECT o from OrderCancellationRequest o where o.order=:order and o.isActive=true")
	OrderCancellationRequest findByOrderActive(@Param("order") Order order);
	
	@Query(value="SELECT o from OrderCancellationRequest o where o.order.product.seller=:seller and o.isActive=true")
	List<OrderCancellationRequest> findBySeller(@Param("seller") Seller seller);
	
}