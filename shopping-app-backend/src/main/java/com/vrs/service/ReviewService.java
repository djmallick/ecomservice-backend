package com.vrs.service;

import com.vrs.dto.ReviewDto;
import com.vrs.payload.ReviewPagedResponse;
import com.vrs.payload.ReviewResponse;

public interface ReviewService {
	
	//customer can create review
	ReviewResponse createReview(ReviewDto reviewDto, Integer orderId);
	
	//customer can update review
	ReviewResponse updateReview(Integer reviewId,ReviewDto reviewDto);
	
	//customer and admin can delete review
	boolean deleteReview(Integer reviewId);
	
	ReviewResponse getReviewById(Integer reviewId);
	
	//Customer want to see review based on order
	ReviewResponse getReviewByOrderId(Integer orderId);
	
	//Users and customers want to see review based on product search
	ReviewPagedResponse getReviewByProductId(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, Integer productId);
	
	//Customer wants to see all the previous reviews submitted
	ReviewPagedResponse getReviewByCustomerId(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, Integer customerId);
	
	//Seller want to see all the reviews
	ReviewPagedResponse getReviewBySellerId(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, Integer sellerId);

}
