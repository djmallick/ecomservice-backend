package com.vrs.exception;

public class DuplicateEntityInsertionException extends RuntimeException {

	private String entityName;
	private String entityId;
	private String message;
	
	public DuplicateEntityInsertionException(String entityName, String entityId, String message) {
		super(String.format("%s ID: %s - Can not be inserted - %s",entityName, entityId, message));
		this.entityName= entityName;
		this.entityId = entityId;
		this.message = message;
	}

}
