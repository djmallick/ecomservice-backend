package com.vrs.entities;

public enum OrderStatus {
	ACCEPTED("A"),
	SHIPPED("S"),
	CANCELLED("C"),
	DELIVERED("D");
	
	 private String status;

	 private OrderStatus(String status) {
	      this.status = status;
	  }

	    @Override
	    public String toString() {
	        return status;
	    }
	
}
