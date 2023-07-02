package com.vrs.exception;

public class ProductOutOfStockException extends RuntimeException {
	private int productId;
	
	public ProductOutOfStockException(int productId) {
		super(String.format("Product with ID %s out of stock!",productId));
		this.productId= productId;
	}

}
