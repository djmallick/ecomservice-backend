package com.vrs.dto;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ReviewDto {
	
	private int reviewId;
	
	private int productId;
	
	@NotEmpty(message = "Must not be empty")
	@NotNull
	private String description;
	
	@Min(1)
	@Max(5)
	@NotNull
	private byte rating;
	
	private String customerName;
	private Date dateOfReview;
	
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Date getDateOfReview() {
		return dateOfReview;
	}
	public void setDateOfReview(Date dateOfReview) {
		this.dateOfReview = dateOfReview;
	}
	public int getReviewId() {
		return reviewId;
	}
	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public byte getRating() {
		return rating;
	}
	public void setRating(byte rating) {
		this.rating = rating;
	}
	@Override
	public String toString() {
		return "ReviewDto [reviewId=" + reviewId + ", description=" + description + ", rating=" + rating
				+ ", customerName=" + customerName + ", dateOfReview=" + dateOfReview + "]";
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	

}
