package com.vrs.exception;

public class InvalidReviewCreationException extends RuntimeException {
	private String entity;
	private String message;
	
	public InvalidReviewCreationException(String entity, String message) {
		super(String.format("%s can not be submitted - %s",entity, message));
		this.entity= entity;
		this.message = message;
	}

}
