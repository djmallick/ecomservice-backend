package com.vrs.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vrs.dto.CustomerDto;
import com.vrs.dto.SellerDto;
import com.vrs.dto.UserDto;
import com.vrs.payload.ApiResponse;
import com.vrs.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;
	
//	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/seller")
	public ResponseEntity<List<SellerDto>> getAllSellers() {
		List<SellerDto> allSellers = userService.getAllSellers();
		return new ResponseEntity<List<SellerDto>>(allSellers, HttpStatus.OK);	
	}
	
//	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/customer")
	public ResponseEntity<List<CustomerDto>> getAllCustomers() {
		List<CustomerDto> allCustomers = userService.getAllCustomers();
		return new ResponseEntity<List<CustomerDto>>(allCustomers, HttpStatus.OK);	
	}
	
//	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		List<UserDto> allUsers = userService.getAllUsers();
		return new ResponseEntity<List<UserDto>>(allUsers, HttpStatus.OK);	
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> activateUser(@PathVariable Integer userId, @RequestBody UserDto userDto){
		UserDto updatedUser = userService.updateUser(userDto, userId);
		return new ResponseEntity<>(updatedUser, HttpStatus.ACCEPTED);	
	}
	
	@PostMapping("/seller")
	public ResponseEntity<SellerDto> createSeller(@Valid @RequestBody SellerDto sellerDto) {
		SellerDto createdSellerDto = this.userService.createSeller(sellerDto);
		return new ResponseEntity<SellerDto>(createdSellerDto, HttpStatus.CREATED);	
	}
	
	@PostMapping("/customer")
	public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
		CustomerDto createdCustomerDto = this.userService.createCustomer(customerDto);
		return new ResponseEntity<CustomerDto>(createdCustomerDto, HttpStatus.CREATED);	
	}
	
	@PutMapping("/seller/{sellerId}")
	public ResponseEntity<SellerDto> updateSeller(@Valid @RequestBody SellerDto sellerDto, @PathVariable Integer sellerId) {
		SellerDto updatedSellerDto = this.userService.updateSeller(sellerDto, sellerId);
		return new ResponseEntity<SellerDto>(updatedSellerDto, HttpStatus.ACCEPTED);	
	}
	
	
	@PutMapping("/customer/{customerId}")
	public ResponseEntity<CustomerDto> updateCustomer(@Valid @RequestBody CustomerDto customerDto,@PathVariable Integer customerId) {
		CustomerDto updatedCustomerDto = this.userService.updateCustomer(customerDto, customerId);
		return new ResponseEntity<CustomerDto>(updatedCustomerDto, HttpStatus.ACCEPTED);	
	}
	
	@DeleteMapping("/customer/{customerId}")
	public ResponseEntity<ApiResponse> deleteCustomer(@PathVariable Integer customerId) {
		this.userService.deleteCustomer(customerId);
		return new ResponseEntity<>(new ApiResponse("Customer deleted successfully!",true), HttpStatus.ACCEPTED);	
	}
	
	@DeleteMapping("/user/{sellerId}")
	public ResponseEntity<ApiResponse> deleteSeller(@PathVariable Integer sellerId) {
		this.userService.deleteSeller(sellerId);
		return new ResponseEntity<>(new ApiResponse("Seller deleted successfully!",true), HttpStatus.ACCEPTED);	
	}
	
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Integer customerId) {
		CustomerDto customerDto = this.userService.getCustomerById(customerId);
		return new ResponseEntity<>(customerDto, HttpStatus.OK);	
	}
	
	@GetMapping("/seller/{sellerId}")
	public ResponseEntity<SellerDto> getSellerById(@PathVariable Integer sellerId) {
		SellerDto sellerDto = this.userService.getSellerById(sellerId);
		return new ResponseEntity<>(sellerDto, HttpStatus.OK);	
	}
	
	
}
