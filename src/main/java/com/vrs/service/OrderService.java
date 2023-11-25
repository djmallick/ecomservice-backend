package com.vrs.service;

import java.util.List;

import com.vrs.dto.OrderCancellationRequestDto;
import com.vrs.dto.OrderDto;
import com.vrs.payload.OrderPagedResponse;

public interface OrderService {
	
	OrderDto createOrder(OrderDto orderDto);
	OrderDto updateOrderStatus(OrderDto orderDto, Integer orderId);
	OrderDto updateOrderAddress(OrderDto orderDto, Integer orderId);
	boolean deleteOrder(Integer orderId);
	OrderDto getOrderById(Integer orderId);
	OrderPagedResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	OrderPagedResponse getOrdersByCustomerId(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
			Integer customerId, Boolean active);
	OrderPagedResponse getOrdersBySellerId(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
			Integer customerId, Boolean active);
	OrderPagedResponse getOrdersByProductId(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
			Integer productId, Boolean active);
	OrderCancellationRequestDto requestForCancelOrder(OrderCancellationRequestDto request, Integer orderId);
	List<OrderCancellationRequestDto> getCancellationRequestDetails(Integer userId);
	
}
