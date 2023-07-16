package com.vrs.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.vrs.dto.ReviewDto;
import com.vrs.entities.Customer;
import com.vrs.entities.Order;
import com.vrs.entities.OrderStatus;
import com.vrs.entities.Product;
import com.vrs.entities.Review;
import com.vrs.entities.Seller;
import com.vrs.exception.InvalidReviewCreationException;
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
		
		if(!reviewRepo.findByOrder(order).isEmpty()) {
			throw new InvalidReviewCreationException("Review","Review already submitted for this order");
		}
		
		Review review = new Review();
		review.setCustomer(order.getCustomer());
		review.setOrder(order);
		review.setDateOfReview(new Date());
		review.setRating(reviewDto.getRating());
		review.setDescription(reviewDto.getDescription());
		
		Review savedReview = reviewRepo.save(review);
		updateProductRating(savedReview);
		return new ReviewResponse(true, "Review submitted successfully", reviewToReviewDto(savedReview));
	}

	@Override
	public ReviewResponse updateReview(Integer reviewId, ReviewDto reviewDto) {
		Review review = reviewRepo.findById(reviewId).orElseThrow(()-> new ResourceNotFoundException("Review","id", reviewId));
		review.setDescription(reviewDto.getDescription());
		review.setDateOfReview(new Date());
		review.setRating(reviewDto.getRating());
		Review updatedReview = reviewRepo.save(review);
		updateProductRating(updatedReview);
		return new ReviewResponse(true, "Review updated successfully", reviewToReviewDto(updatedReview));
	}

	@Override
	public boolean deleteReview(Integer reviewId) {
		Review review = reviewRepo.findById(reviewId).orElseThrow(()-> new ResourceNotFoundException("Review","id", reviewId));
		reviewRepo.delete(review);
		Optional<Review> deletedReview = reviewRepo.findById(reviewId);
		if(deletedReview.isEmpty()) {
			updateProductRating(review);
		}
		return deletedReview.isEmpty();
	}

	@Override
	public ReviewResponse getReviewById(Integer reviewId) {
		Review review = reviewRepo.findById(reviewId).orElseThrow(()-> new ResourceNotFoundException("Review","id", reviewId));
		return new ReviewResponse(true, "Review fetched successfully", reviewToReviewDto(review));
	}

	@Override
	public ReviewResponse getReviewByOrderId(Integer orderId) {
		Order order = orderRepo.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order","id", orderId));
		Review review = reviewRepo.findByOrder(order).orElseThrow(()-> new ResourceNotFoundException("Review","order id", orderId));
		return new ReviewResponse(true, "Review fetched successfully", reviewToReviewDto(review));
	}

	@Override
	public ReviewPagedResponse getReviewByProductId(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,Integer productId) {
		Product product = productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product","id", productId));
		Pageable p = getPageable(pageNumber, pageSize, sortBy, sortDir);
		Page<Review> pageReviews = reviewRepo.findByProduct(product, p);		
		return createReviewPagedResponse(pageReviews);
	}

	@Override
	public ReviewPagedResponse getReviewByCustomerId(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, Integer customerId) {
		Customer customer = customerRepo.findById(customerId).orElseThrow(()-> new ResourceNotFoundException("Customer","id", customerId));
		Pageable p = getPageable(pageNumber, pageSize, sortBy, sortDir);
		Page<Review> pageReviews = reviewRepo.findByCustomer(customer, p);		
		return createReviewPagedResponse(pageReviews);
	}

	@Override
	public ReviewPagedResponse getReviewBySellerId(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, Integer sellerId) {
		Seller seller = sellerRepo.findById(sellerId).orElseThrow(()-> new ResourceNotFoundException("Seller","id", sellerId));
		Pageable p = getPageable(pageNumber, pageSize, sortBy, sortDir);
		Page<Review> pageReviews = reviewRepo.findBySeller(seller, p);		
		return createReviewPagedResponse(pageReviews);
	}

	
	
	private void updateProductRating(Review review) {
		Product product = review.getOrder().getProduct();
		Double averageRating = reviewRepo.findAverageRatingByProduct(product);
		if(averageRating==null) {
			product.setAverageRating(BigDecimal.valueOf(0)
				    .setScale(1, RoundingMode.HALF_UP)
				    .doubleValue());
		}else {
			product.setAverageRating(BigDecimal.valueOf(averageRating)
				    .setScale(1, RoundingMode.HALF_UP)
				    .doubleValue());
		}
		productRepo.save(product);
	}
	
	public ReviewDto reviewToReviewDto(Review review) {
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setDescription(review.getDescription());
		reviewDto.setDateOfReview(review.getDateOfReview());
		reviewDto.setRating(review.getRating());
		reviewDto.setReviewId(review.getReviewId());
		reviewDto.setProductId(review.getOrder().getProduct().getProductId());
		reviewDto.setCustomerName(review.getCustomer().getFirstName()+" "+review.getCustomer().getLastName());
		return reviewDto;
	}
	
	private ReviewPagedResponse createReviewPagedResponse(Page<Review> pageReviews) {
		List<Review> reviews = pageReviews.getContent();
		List<ReviewDto> fetchedReviews = new ArrayList<ReviewDto>();
		for(Review review:reviews) {
			ReviewDto fetchedReviewDto = reviewToReviewDto(review);
			fetchedReviews.add(fetchedReviewDto);
		}
		ReviewPagedResponse reviewPagedResponse = new ReviewPagedResponse();
		reviewPagedResponse.setContents(fetchedReviews);
		reviewPagedResponse.setPageNumber(pageReviews.getNumber());
		reviewPagedResponse.setPageSize(pageReviews.getSize());
		reviewPagedResponse.setTotalElements(pageReviews.getTotalElements());
		reviewPagedResponse.setLastPage(pageReviews.isLast());
		reviewPagedResponse.setTotalPages(pageReviews.getTotalPages());
		reviewPagedResponse.setSuccessful(true);
		reviewPagedResponse.setMessage("Fetched successfully");
		return reviewPagedResponse;
	}
	
	private Pageable getPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		Sort sort = null;
		if(sortDir.equals("desc")) {
			sort = Sort.by(sortBy).descending();
		}
		else {
			sort = Sort.by(sortBy).ascending();
		}
		return PageRequest.of(pageNumber, pageSize, sort);
		
	}
	
}
