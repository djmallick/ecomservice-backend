package com.vrs.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vrs.config.AppConstants;
import com.vrs.dto.ReviewDto;
import com.vrs.payload.OrderPagedResponse;
import com.vrs.payload.ReviewPagedResponse;
import com.vrs.payload.ReviewResponse;
import com.vrs.service.ReviewService;

@RestController
@RequestMapping("/api")
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@PostMapping("/order/{orderId}/review")
	public ResponseEntity<ReviewResponse> createReview(
			@Valid @RequestBody ReviewDto reviewDto, @PathVariable Integer orderId) {
		ReviewResponse createdReviewResponse = reviewService.createReview(reviewDto, orderId);
		return new ResponseEntity<ReviewResponse>(createdReviewResponse, HttpStatus.CREATED);	
	}
	
	@PutMapping("/review/{reviewId}")
	public ResponseEntity<ReviewResponse> updateReview(
			@Valid @RequestBody ReviewDto reviewDto, @PathVariable Integer reviewId) {
		ReviewResponse updatedReviewResponse = reviewService.updateReview(reviewId, reviewDto);
		return new ResponseEntity<ReviewResponse>(updatedReviewResponse, HttpStatus.OK);	
	}
	
	@DeleteMapping("/review/{reviewId}")
	public ResponseEntity<ReviewResponse> deleteReview(
			@PathVariable Integer reviewId) {
		boolean isDeleted = reviewService.deleteReview(reviewId);
		if(isDeleted) {
			return new ResponseEntity<ReviewResponse>(new ReviewResponse(true, "Review deleted successfully", null), HttpStatus.OK);
		}
		return new ResponseEntity<ReviewResponse>(new ReviewResponse(false, "There is an issue to delete the product", null), HttpStatus.NOT_IMPLEMENTED);
	}
	
	@GetMapping("/review/product/{productId}")
	public ResponseEntity<ReviewPagedResponse> getReviewByProductId(
			@RequestParam(value="pageNumber", defaultValue=AppConstants.PAGE_NUMBER, required =false) Integer pageNumber,
			@RequestParam(value="pageSize", defaultValue=AppConstants.PAGE_SIZE, required =false) Integer pageSize,
			@RequestParam(value="sortBy", defaultValue=AppConstants.REVIEW_SORT_BY, required=false) String sortBy,
			@RequestParam(value="sortDir", defaultValue=AppConstants.SORT_DIR, required=false) String sortDir,
			@PathVariable Integer productId
			) {
		ReviewPagedResponse allReviews = reviewService.getReviewByProductId(pageNumber, pageSize, sortBy, sortDir, productId);
		return new ResponseEntity<ReviewPagedResponse>(allReviews, HttpStatus.OK);
	}
	
	@GetMapping("/review/seller/{sellerId}")
	public ResponseEntity<ReviewPagedResponse> getReviewBySellerId(
			@RequestParam(value="pageNumber", defaultValue=AppConstants.PAGE_NUMBER, required =false) Integer pageNumber,
			@RequestParam(value="pageSize", defaultValue=AppConstants.PAGE_SIZE, required =false) Integer pageSize,
			@RequestParam(value="sortBy", defaultValue=AppConstants.REVIEW_SORT_BY, required=false) String sortBy,
			@RequestParam(value="sortDir", defaultValue=AppConstants.SORT_DIR, required=false) String sortDir,
			@PathVariable Integer sellerId
			) {
		ReviewPagedResponse allReviews = reviewService.getReviewBySellerId(pageNumber, pageSize, sortBy, sortDir, sellerId);
		return new ResponseEntity<ReviewPagedResponse>(allReviews, HttpStatus.OK);
	}
	
	@GetMapping("/review/customer/{customerId}")
	public ResponseEntity<ReviewPagedResponse> getReviewByCustomerId(
			@RequestParam(value="pageNumber", defaultValue=AppConstants.PAGE_NUMBER, required =false) Integer pageNumber,
			@RequestParam(value="pageSize", defaultValue=AppConstants.PAGE_SIZE, required =false) Integer pageSize,
			@RequestParam(value="sortBy", defaultValue=AppConstants.REVIEW_SORT_BY, required=false) String sortBy,
			@RequestParam(value="sortDir", defaultValue=AppConstants.SORT_DIR, required=false) String sortDir,
			@PathVariable Integer customerId
			) {
		ReviewPagedResponse allReviews = reviewService.getReviewByCustomerId(pageNumber, pageSize, sortBy, sortDir, customerId);
		return new ResponseEntity<ReviewPagedResponse>(allReviews, HttpStatus.OK);
	}
	
	
	@GetMapping("/review/order/{orderId}")
	public ResponseEntity<ReviewResponse> getReviewByOrderId(@PathVariable Integer orderId) {
		ReviewResponse reviewByOrderId = reviewService.getReviewByOrderId(orderId);
		return new ResponseEntity<ReviewResponse>(reviewByOrderId, HttpStatus.OK);
	}
	
	@GetMapping("/review/{reviewId}")
	public ResponseEntity<ReviewResponse> getReviewById(@PathVariable Integer reviewId) {
		ReviewResponse reviewByReviewId = reviewService.getReviewById(reviewId);
		return new ResponseEntity<ReviewResponse>(reviewByReviewId, HttpStatus.OK);
	}
	
	
}
