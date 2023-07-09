package com.vrs.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vrs.payload.ApiResponse;
import com.vrs.payload.OrderResponse;
import com.vrs.payload.ReviewResponse;


@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message, false);
		return new ResponseEntity<ApiResponse> (apiResponse,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiResponse> badCredentialsExceptionHandler(BadCredentialsException ex){
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message, false);
		return new ResponseEntity<ApiResponse> (apiResponse,HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<ApiResponse> disabledExceptionHandler(DisabledException ex){
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message, false);
		return new ResponseEntity<ApiResponse> (apiResponse,HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex){
		Map<String, String> map = new HashMap<String, String>();
		ex.getBindingResult().getAllErrors().forEach((error)->{
			String errorField = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			map.put(errorField, message);
		});
		return new ResponseEntity<Map<String,String>> (map,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ProductOutOfStockException.class)
	public ResponseEntity<OrderResponse> productOutOfStockExceptionExceptionHandler(ProductOutOfStockException ex){
		String message = ex.getMessage();
		OrderResponse orderResponse = new OrderResponse(false, message, null);
		return new ResponseEntity<OrderResponse> (orderResponse,HttpStatus.NOT_IMPLEMENTED);
	}
	
	@ExceptionHandler(InvalidUpdateException.class)
	public ResponseEntity<OrderResponse> invalidUpdateExceptionHandler(InvalidUpdateException ex){
		String message = ex.getMessage();
		OrderResponse orderResponse = new OrderResponse(false, message, null);
		return new ResponseEntity<OrderResponse> (orderResponse,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidReviewCreationException.class)
	public ResponseEntity<ReviewResponse> invalidReviewCreationExceptionHandler(InvalidReviewCreationException ex){
		String message = ex.getMessage();
		ReviewResponse reviewResponse = new ReviewResponse(false, message, null);
		return new ResponseEntity<ReviewResponse> (reviewResponse,HttpStatus.BAD_REQUEST);
	}
}
