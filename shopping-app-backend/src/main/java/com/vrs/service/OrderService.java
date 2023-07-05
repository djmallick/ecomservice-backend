package com.vrs.service;

import java.util.List;


import com.vrs.dto.OrderDto;
import com.vrs.payload.OrderPagedResponse;

public interface OrderService {
	
	OrderDto createOrder(OrderDto orderDto);
	OrderDto updateOrderStatus(OrderDto orderDto, Integer orderId);
	OrderDto updateOrderAddress(OrderDto orderDto, Integer orderId);
	boolean deleteOrder(Integer orderId);
	OrderDto getOrderById(Integer orderId);
	List<OrderDto> getOrdersByCustomerId(Integer customerId);
	List<OrderDto> getOrdersByCustomerId(Integer customerId, boolean isActive);
	List<OrderDto> getOrdersBySellerId(Integer sellerId);
	List<OrderDto> getOrdersBySellerId(Integer sellerId, boolean isActive);
	List<OrderDto> getOrdersByProductId(Integer productId);
	OrderPagedResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	
}
