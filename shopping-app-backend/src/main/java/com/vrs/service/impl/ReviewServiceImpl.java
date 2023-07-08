package com.vrs.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vrs.dto.ReviewDto;
import com.vrs.entities.Customer;
import com.vrs.entities.Order;
import com.vrs.entities.OrderStatus;
import com.vrs.entities.Product;
import com.vrs.entities.Review;
import com.vrs.exception.InvalidReviewCreationException;
import com.vrs.exception.InvalidUpdateException;
import com.vrs.exception.ResourceNotFoundException;
import com.vrs.payload.ReviewPagedResponse;
import com.vrs.payload.ReviewResponse;
import com.vrs.repositories.CustomerRepo;
import com.vrs.repositories.OrderRepo;
import com.vrs.repositories.ProductRepo;
import com.vrs.repositories.ReviewRepo;
import com.vrs.repositories.SellerRepo;
import com.vrs.repositories.UserRepo;
import com.vrs.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	SellerRepo sellerRepo;
	
	@Autowired
	OrderRepo orderRepo;
	
	@Autowired
	CustomerRepo customerRepo;
	
	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	ReviewRepo reviewRepo;
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	public ReviewResponse createReview(ReviewDto reviewDto, Integer orderId) {
		Order order = orderRepo.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order","id", orderId));
		if(!order.getStatus().equals(OrderStatus.DELIVERED)) {
			throw new InvalidReviewCreationException("Review","Order status should be delivered");
		}
		Review review = new Review();
		review.setCustomer(order.getCustomer());
		review.setOrder(order);
		review.setDateOfReview(new Date());
		review.setRating(reviewDto.getRating());
		review.setDescription(reviewDto.getDescription());
		
		Review savedReview = reviewRepo.save(review);
		updateProductRating(order.getProduct().getProductId(), savedReview.getRating());
		return new ReviewResponse(true, "Review submitted successfully", reviewToReviewDto(savedReview));
	}

	@Override
	public ReviewResponse updateReview(Integer reviewId, ReviewDto reviewDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReviewResponse deleteReview(Integer reviewId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReviewPagedResponse getReviewById(Integer reviewId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReviewPagedResponse getReviewByOrderId(Integer orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReviewPagedResponse getReviewByProductId(Integer productId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReviewPagedResponse getReviewByCustomerId(Integer customerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReviewPagedResponse getReviewBySellerId(Integer sellerId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void updateProductRating(int productId, byte customerRating) {
		if(customerRating>=1 && customerRating<=5) {
			Product product = productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product","id", productId));
			double averageRating = product.getAverageRating();
			if(averageRating==0) {
				averageRating = customerRating;
			} else {
				averageRating = ((averageRating + customerRating))/2;
			}
			product.setAverageRating(BigDecimal.valueOf(averageRating)
				    .setScale(1, RoundingMode.HALF_UP)
				    .doubleValue());
			
			productRepo.save(product);
		}
	}
	
	public ReviewDto reviewToReviewDto(Review review) {
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setDescription(review.getDescription());
		reviewDto.setDateOfReview(review.getDateOfReview());
		reviewDto.setRating(review.getRating());
		reviewDto.setCustomerName(review.getCustomer().getFirstName()+" "+review.getCustomer().getLastName());
		return reviewDto;
	}
}
