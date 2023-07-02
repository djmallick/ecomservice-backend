package com.vrs.service;

import java.util.List;

import com.vrs.dto.OrderDto;

public interface OrderService {
	
	OrderDto createOrder(Integer productId, Integer customerId);
	OrderDto updateOrder(OrderDto orderDto, Integer orderId);
	boolean deleteOrder(Integer orderId);
	OrderDto getOrderById(Integer orderId);
	List<OrderDto> getAllOrders();
	List<OrderDto> getOrdersByCustomerId(Integer customerId);
	List<OrderDto> getOrdersByCustomerId(Integer customerId, boolean isActive);
	List<OrderDto> getOrdersBySellerId(Integer sellerId);
	List<OrderDto> getOrdersBySellerId(Integer sellerId, boolean isActive);
	List<OrderDto> getOrdersByProductId(Integer productId);
	
}
