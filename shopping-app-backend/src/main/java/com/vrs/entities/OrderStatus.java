package com.vrs.entities;

public enum OrderStatus {
	//FLOW ->  placed->accepted->shipped->delivered
	PLACED("P"),
	ACCEPTED("A"),
	SHIPPED("S"),
	CANCEL_REQUESTED("R"),
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
