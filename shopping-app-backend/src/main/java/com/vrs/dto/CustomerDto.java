package com.vrs.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CustomerDto {
	
	private int customer_Id;
	
	@NotEmpty(message = "Must not be empty")
	@NotNull
	private String firstName;
	
	@NotNull
	@Valid private UserDto userDto;
	
	@NotEmpty(message = "Must not be empty")
	@NotNull
	private String lastName;
	
	@Size(min = 10, max = 10)
	private String mobileNumber;
	
	private Date dateOfBirth;
	
	private List<OrderDto> orderDtos = new ArrayList<>();
	
	private List<ReviewDto> reviewDtos = new ArrayList<>();

	public int getCustomer_Id() {
		return customer_Id;
	}

	public void setCustomer_Id(int customer_Id) {
		this.customer_Id = customer_Id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public UserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public List<OrderDto> getOrderDtos() {
		return orderDtos;
	}

	public void setOrderDtos(List<OrderDto> orderDtos) {
		this.orderDtos = orderDtos;
	}

	public List<ReviewDto> getReviewDtos() {
		return reviewDtos;
	}

	public void setReviewDtos(List<ReviewDto> reviewDtos) {
		this.reviewDtos = reviewDtos;
	}
	
	

}
