package com.vrs.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
}
