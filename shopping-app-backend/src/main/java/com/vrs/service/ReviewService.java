package com.vrs.service;

import com.vrs.dto.ReviewDto;
import com.vrs.payload.ReviewPagedResponse;
import com.vrs.payload.ReviewResponse;

public interface ReviewService {
	ReviewResponse createReview(ReviewDto reviewDto, Integer orderId);
	ReviewResponse updateReview(Integer reviewId,ReviewDto reviewDto);
	boolean deleteReview(Integer reviewId);
	
	ReviewResponse getReviewById(Integer reviewId);
	
	ReviewResponse getReviewByOrderId(Integer orderId);
	
	ReviewPagedResponse getReviewByProductId(Integer productId);
	
	ReviewPagedResponse getReviewByCustomerId(Integer customerId);
	
	ReviewPagedResponse getReviewBySellerId(Integer sellerId);
}
