package com.vrs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vrs.entities.Order;
import com.vrs.entities.OrderCancellationRequest;

public interface OrderCancellationRequestRepo extends JpaRepository<OrderCancellationRequest, Integer> {
	OrderCancellationRequest findByOrder(Order order);
}
