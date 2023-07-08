package com.vrs.payload;

import com.vrs.dto.ReviewDto;

public class ReviewResponse {
	
	boolean isSuccessful;
	String message;
	ReviewDto review;
	
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
	
	
	public ReviewDto getReview() {
		return review;
	}
	public void setReview(ReviewDto review) {
		this.review = review;
	}
	public ReviewResponse(boolean isSuccessful, String message, ReviewDto reviewDto) {
		super();
		this.isSuccessful = isSuccessful;
		this.message = message;
		this.review = reviewDto;
	}

}
