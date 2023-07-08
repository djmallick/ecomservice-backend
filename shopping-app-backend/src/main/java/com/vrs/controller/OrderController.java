package com.vrs.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vrs.config.AppConstants;
import com.vrs.dto.OrderDto;
import com.vrs.payload.OrderPagedResponse;
import com.vrs.payload.OrderResponse;
import com.vrs.service.OrderService;

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
	
	@PutMapping("/order/{orderId}/address")
	public ResponseEntity<OrderResponse> updateOrderAddress(
			@Valid @RequestBody OrderDto orderDto, @PathVariable Integer orderId) {
		OrderDto updatedOrder = orderService.updateOrderAddress(orderDto, orderId);
		OrderResponse response = new OrderResponse(true, "Order Address updated successfully", updatedOrder);
		return new ResponseEntity<OrderResponse>(response, HttpStatus.OK);	
	}
	
	@PutMapping("/order/{orderId}/status")
	public ResponseEntity<OrderResponse> updateOrderStatus(
			@RequestBody OrderDto orderDto, @PathVariable Integer orderId) {
		OrderDto updatedOrder = orderService.updateOrderStatus(orderDto, orderId);
		OrderResponse response = new OrderResponse(true, "Order status updated successfully", updatedOrder);
		return new ResponseEntity<OrderResponse>(response, HttpStatus.OK);	
	}
	
	@DeleteMapping("/order/{orderId}")
	public ResponseEntity<OrderResponse> deleteOrder(
			@PathVariable Integer orderId) {
		boolean isDeleted = orderService.deleteOrder(orderId);
		if(isDeleted) {
			return new ResponseEntity<OrderResponse>(new OrderResponse(true, "Order deleted successfully", null), HttpStatus.OK);	
		}
		return new ResponseEntity<OrderResponse>(new OrderResponse(false, "There is an issue to delete the product",null), HttpStatus.NOT_IMPLEMENTED);	
	}
	
	@GetMapping("/order/{orderId}")
	public ResponseEntity<OrderResponse> getOrderById(
			@PathVariable Integer orderId) {
		OrderDto orderDto = orderService.getOrderById(orderId);
		return new ResponseEntity<OrderResponse>(new OrderResponse(true, "Fetched successfully", orderDto), HttpStatus.OK);	
	}
	
	@GetMapping("/order")
	public ResponseEntity<OrderPagedResponse> getAllOrders(
			@RequestParam(value="pageNumber", defaultValue=AppConstants.PAGE_NUMBER, required =false) Integer pageNumber,
			@RequestParam(value="pageSize", defaultValue=AppConstants.PAGE_SIZE, required =false) Integer pageSize,
			@RequestParam(value="sortBy", defaultValue=AppConstants.ORDER_SORT_BY, required=false) String sortBy,
			@RequestParam(value="sortDir", defaultValue=AppConstants.SORT_DIR, required=false) String sortDir
			) {
		OrderPagedResponse allOrders = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<OrderPagedResponse>(allOrders, HttpStatus.OK);	
	}
	
	@GetMapping("/order/customer/{customerId}")
	public ResponseEntity<OrderPagedResponse> getOrdersByCustomerId(
			@RequestParam(value="pageNumber", defaultValue=AppConstants.PAGE_NUMBER, required =false) Integer pageNumber,
			@RequestParam(value="pageSize", defaultValue=AppConstants.PAGE_SIZE, required =false) Integer pageSize,
			@RequestParam(value="sortBy", defaultValue=AppConstants.ORDER_SORT_BY, required=false) String sortBy,
			@RequestParam(value="sortDir", defaultValue=AppConstants.SORT_DIR, required=false) String sortDir,
			@RequestParam(value="active", required =false) Boolean active,
			@PathVariable Integer customerId
			) {
		OrderPagedResponse allOrders = orderService.getOrdersByCustomerId(pageNumber, pageSize, sortBy, sortDir, customerId, active);
		return new ResponseEntity<OrderPagedResponse>(allOrders, HttpStatus.OK);	
	}
	
	@GetMapping("/order/product/{productId}")
	public ResponseEntity<OrderPagedResponse> getOrdersByProductId(
			@RequestParam(value="pageNumber", defaultValue=AppConstants.PAGE_NUMBER, required =false) Integer pageNumber,
			@RequestParam(value="pageSize", defaultValue=AppConstants.PAGE_SIZE, required =false) Integer pageSize,
			@RequestParam(value="sortBy", defaultValue=AppConstants.ORDER_SORT_BY, required=false) String sortBy,
			@RequestParam(value="sortDir", defaultValue=AppConstants.SORT_DIR, required=false) String sortDir,
			@RequestParam(value="active", required =false) Boolean active,
			@PathVariable Integer productId
			) {
		OrderPagedResponse allOrders = orderService.getOrdersByProductId(pageNumber, pageSize, sortBy, sortDir, productId, active);
		return new ResponseEntity<OrderPagedResponse>(allOrders, HttpStatus.OK);
	}
	
	@GetMapping("/order/seller/{sellerId}")
	public ResponseEntity<OrderPagedResponse> getOrdersBySellerId(
			@RequestParam(value="pageNumber", defaultValue=AppConstants.PAGE_NUMBER, required =false) Integer pageNumber,
			@RequestParam(value="pageSize", defaultValue=AppConstants.PAGE_SIZE, required =false) Integer pageSize,
			@RequestParam(value="sortBy", defaultValue=AppConstants.ORDER_SORT_BY, required=false) String sortBy,
			@RequestParam(value="sortDir", defaultValue=AppConstants.SORT_DIR, required=false) String sortDir,
			@RequestParam(value="active", required =false) Boolean active,
			@PathVariable Integer sellerId
			) {
		OrderPagedResponse allOrders = orderService.getOrdersBySellerId(pageNumber, pageSize, sortBy, sortDir, sellerId, active);
		return new ResponseEntity<OrderPagedResponse>(allOrders, HttpStatus.OK);
	}

	
}
