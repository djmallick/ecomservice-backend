package com.vrs.dto;

import java.util.Date;

import javax.validation.constraints.Size;

public class ComplainDto {
	
	private int complainId;
	
	@Size(max=100, message ="Maximum length of description should be 100")
	private String description;
	
	private Date dateOfComplain;
	
	private String status;

	private OrderDto orderDto;

	public int getComplainId() {
		return complainId;
	}

	public void setComplainId(int complainId) {
		this.complainId = complainId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateOfComplain() {
		return dateOfComplain;
	}

	public void setDateOfComplain(Date dateOfComplain) {
		this.dateOfComplain = dateOfComplain;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public OrderDto getOrderDto() {
		return orderDto;
	}

	public void setOrderDto(OrderDto orderDto) {
		this.orderDto = orderDto;
	}
	

}
