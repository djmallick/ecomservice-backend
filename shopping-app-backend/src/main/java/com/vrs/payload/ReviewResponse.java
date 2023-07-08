package com.vrs.payload;

import com.vrs.dto.ReviewDto;

public class ReviewResponse {
	
	boolean isSuccessful;
	String message;
	ReviewDto reviewDto;
	
	public boolean isSuccessful() {
		return isSuccessful;
	}
	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ReviewDto getReviewDto() {
		return reviewDto;
	}
	public void setReviewDto(ReviewDto reviewDto) {
		this.reviewDto = reviewDto;
	}
	
	
	
	public ReviewResponse(boolean isSuccessful, String message, ReviewDto reviewDto) {
		super();
		this.isSuccessful = isSuccessful;
		this.message = message;
		this.reviewDto = reviewDto;
	}
	@Override
	public String toString() {
		return "ReviewResponse [isSuccessful=" + isSuccessful + ", message=" + message + ", reviewDto=" + reviewDto
				+ ", isSuccessful()=" + isSuccessful() + ", getMessage()=" + getMessage() + ", getReviewDto()="
				+ getReviewDto() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
}
