package com.vrs.exception;

public class InvalidUpdateException extends RuntimeException {
	private String entity;
	private int entityId;
	private String message;
	
	public InvalidUpdateException(String entity, int entityId, String message) {
		super(String.format("%s ID: %s - Can not be updated - %s",entity, entityId, message));
		this.entity= entity;
		this.entityId = entityId;
		this.message = message;
	}

}
