package com.vrs.exception;

public class ProductOutOfStockException extends RuntimeException {
	private int productId;
	private String message;
	
	public ProductOutOfStockException(int productId, String message) {
		super(String.format("Product ID: %s - %s",productId, message));
		this.productId= productId;
		this.message = message;
	}

}
