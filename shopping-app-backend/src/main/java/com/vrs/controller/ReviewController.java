package com.vrs.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vrs.dto.ReviewDto;
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
	
	
}
