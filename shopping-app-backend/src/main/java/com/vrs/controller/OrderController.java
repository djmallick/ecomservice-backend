package com.vrs.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vrs.dto.OrderDto;
import com.vrs.dto.ProductDto;
import com.vrs.service.OrderService;
import com.vrs.service.ProductService;

@RestController
@RequestMapping("/api")
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@PostMapping("/product/{productId}/order/customer/{customerId}")
	public ResponseEntity<OrderDto> createOrder(
			@PathVariable Integer productId, @PathVariable Integer customerId) {
		OrderDto createdOrder = orderService.createOrder(productId, customerId);
		return new ResponseEntity<OrderDto>(createdOrder, HttpStatus.CREATED);	
	}
	
}
