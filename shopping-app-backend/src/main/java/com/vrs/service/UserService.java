package com.vrs.service;

import java.util.List;

import com.vrs.dto.CustomerDto;
import com.vrs.dto.SellerDto;
import com.vrs.dto.UserDto;


public interface UserService {
	
	UserDto createUser(UserDto userDto);
	
	UserDto updateUser(UserDto userDto, Integer userId);
	
	UserDto getUserByUserId(Integer userId);
	
	List<UserDto> getAllUsers();
	
	void deleteUser(Integer userId);
	
	
	//Seller related methods
	
	SellerDto createSeller(SellerDto sellerDto);
	
	SellerDto updateSeller(SellerDto sellerDto, Integer sellerId);
	
	void deleteSeller(Integer sellerId);
	
	List<SellerDto> getAllSellers();
	
	SellerDto getSellerById(Integer sellerId);
	
	
	//Customer related methods

	CustomerDto createCustomer(CustomerDto customerDto);

	CustomerDto updateCustomer(CustomerDto customerDto, Integer customerId);

	void deleteCustomer(Integer customerId);
	
	List<CustomerDto> getAllCustomers();

	CustomerDto getCustomerById(Integer customerId);


	
}
