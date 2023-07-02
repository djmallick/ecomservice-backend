package com.vrs.payload;

import com.vrs.dto.OrderDto;

public class OrderResponse {
	boolean isSuccessful;
	String message;
	OrderDto orderDto;
	public boolean isSuccessful() {
		return isSuccessful;
	}
	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public OrderDto getOrderDto() {
		return orderDto;
	}
	public void setOrderDto(OrderDto orderDto) {
		this.orderDto = orderDto;
	}
	public OrderResponse(boolean isSuccessful, String message, OrderDto orderDto) {
		super();
		this.isSuccessful = isSuccessful;
		this.message = message;
		this.orderDto = orderDto;
	}
	
	
}
