package com.vrs.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vrs.dto.OrderDto;
import com.vrs.dto.ProductDto;
import com.vrs.payload.OrderResponse;
import com.vrs.service.OrderService;
import com.vrs.service.ProductService;

@RestController
@RequestMapping("/api")
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@PostMapping("/order")
	public ResponseEntity<OrderResponse> createOrder(
			@Valid @RequestBody OrderDto orderDto) {
		OrderDto createdOrder = orderService.createOrder(orderDto);
		OrderResponse response = new OrderResponse(true, "Order placed successfully", createdOrder);
		return new ResponseEntity<OrderResponse>(response, HttpStatus.CREATED);	
	}
	
	@PutMapping("/order/{orderId}")
	public ResponseEntity<OrderResponse> updateOrder(
			@Valid @RequestBody OrderDto orderDto, @PathVariable Integer orderId) {
		OrderDto updatedOrder = orderService.updateOrder(orderDto, orderId);
		OrderResponse response = new OrderResponse(true, "Order updated successfully", updatedOrder);
		return new ResponseEntity<OrderResponse>(response, HttpStatus.OK);	
	}
	
	@PutMapping("/order/{orderId}/status")
	public ResponseEntity<OrderResponse> updateOrderStatus(
			@Valid @RequestBody OrderDto orderDto, @PathVariable Integer orderId) {
		OrderDto updatedOrder = orderService.updateOrder(orderDto, orderId);
		OrderResponse response = new OrderResponse(true, "Order updated successfully", updatedOrder);
		return new ResponseEntity<OrderResponse>(response, HttpStatus.OK);	
	}
	
	
}
