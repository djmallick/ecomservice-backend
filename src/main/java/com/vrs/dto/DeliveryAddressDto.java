package com.vrs.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DeliveryAddressDto {
	
	@NotNull
	@NotEmpty(message = "Must not be empty")
	@Size(min = 10, max = 50)
	private String address1;
	@Size(max = 50)
	private String address2;
	@NotNull
	@NotEmpty(message = "Must not be empty")
	private String district;
	@NotNull
	@NotEmpty(message = "Must not be empty")
	private String state;
	@NotNull
	@NotEmpty(message = "Must not be empty")
	@Size(min = 6, max = 6, message = "Must be a valid pincode of 6 digits")
	private String pinCode;
	@NotNull
	@NotEmpty(message = "Must not be empty")
	@Size(min = 10, max = 10, message = "Must be a valid mobile number of 10 digits")
	private String mobileNumber;
	
	
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	
	
	

}
