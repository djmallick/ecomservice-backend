package com.vrs.exception;

public class UnauthorizedResourceAccessException extends RuntimeException{
	private String resourceName;
	private String fieldName;

	public UnauthorizedResourceAccessException(String resourceName, String fieldName) {
		super(String.format("%s update is not allowed with someone else credential: %s",resourceName, fieldName));
		this.resourceName= resourceName;
		this.fieldName = fieldName;
	}

}
