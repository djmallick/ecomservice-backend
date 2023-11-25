package com.vrs.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vrs.dto.CustomerDto;
import com.vrs.dto.SellerDto;
import com.vrs.dto.UserDto;
import com.vrs.entities.Customer;
import com.vrs.entities.Role;
import com.vrs.entities.Seller;
import com.vrs.entities.User;
import com.vrs.exception.DuplicateEntityInsertionException;
import com.vrs.exception.ResourceNotFoundException;
import com.vrs.exception.UnauthorizedResourceAccessException;
import com.vrs.repositories.CustomerRepo;
import com.vrs.repositories.RoleRepository;
import com.vrs.repositories.SellerRepo;
import com.vrs.repositories.UserRepo;
import com.vrs.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	SellerRepo sellerRepo;
	
	@Autowired
	CustomerRepo customerRepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	RoleRepository roleRepo;

	@Override
	public UserDto createUser(UserDto userDto) {
		userDto.setActive(false);
		userDto.setRegistrationDate(new Date());
		User user = userRepo.save(modelMapper.map(userDto, User.class));
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto getUserByUserId(Integer userId) {
		User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User ","id ", userId));	
		user.setPassword(null);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> users = this.userRepo.findAll();
		users.stream().forEach(user->user.setPassword(null));
		List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User ","id ", userId));
		this.userRepo.delete(user);	
	}

	@Override
	public List<SellerDto> getAllSellers() {
		List<Seller> sellers = this.sellerRepo.findAll();
		List<SellerDto> sellerDtos = new ArrayList<SellerDto>();
		for(Seller s:sellers) {
			s.getUser().setPassword(null);
			UserDto u = modelMapper.map(s.getUser(), UserDto.class);
			SellerDto sd = modelMapper.map(s, SellerDto.class);
			sd.setUserId(u.getUserId());
			sd.setEmail(u.getEmail());
			sd.setRoles(s.getUser().getRoles());
			sd.setActive(u.isActive());
			sd.setRegistrationDate(u.getRegistrationDate());
			sellerDtos.add(sd);
		}
		return sellerDtos;
	}
	
	@Override
	public List<CustomerDto> getAllCustomers() {
		List<Customer> customers = this.customerRepo.findAll();
		List<CustomerDto> customerDtos = new ArrayList<CustomerDto>();
		for(Customer c:customers) {
			c.getUser().setPassword(null);
			UserDto u = modelMapper.map(c.getUser(), UserDto.class);
			CustomerDto cd = modelMapper.map(c, CustomerDto.class);
			cd.setUserDto(u);
			customerDtos.add(cd);
		}
		return customerDtos;
	}

	@Override
	public SellerDto createSeller(SellerDto sellerDto) {
		Optional<User> findByEmail = userRepo.findByEmail(sellerDto.getEmail());
		if(findByEmail.isPresent()) {
			throw new DuplicateEntityInsertionException("Email", sellerDto.getEmail(), "Email already exists");
		}
		sellerDto.setActive(false);
		sellerDto.setRegistrationDate(new Date());
		Seller seller = modelMapper.map(sellerDto, Seller.class);
		User user = new User();
		user.setActive(false);
		user.setRegistrationDate(new Date());
		Role role = roleRepo.findByRoleName("ROLE_SELLER");
		
		user.getRoles().add(role);
		user.setEmail(sellerDto.getEmail());
		user.setPassword(sellerDto.getPassword());
		seller.setUser(user);
		Seller savedSeller = sellerRepo.save(seller);	
		User savedUser = savedSeller.getUser();
		
		SellerDto sd =  modelMapper.map(savedSeller, SellerDto.class);
		sd.setRoles(savedUser.getRoles());
		sd.setEmail(savedUser.getEmail());
		sd.setRegistrationDate(savedUser.getRegistrationDate());
		return sd;	
	}
	
	@Override
	public CustomerDto createCustomer(CustomerDto customerDto) {
		customerDto.getUserDto().setActive(true);
		customerDto.getUserDto().setRegistrationDate(new Date());
		Customer customer = modelMapper.map(customerDto, Customer.class);
		Role role = roleRepo.findByRoleName("ROLE_CUSTOMER");
		customer.getUser().getRoles().add(role);
		Customer savedCustomer = customerRepo.save(customer);
		UserDto u = modelMapper.map(savedCustomer.getUser(), UserDto.class);
		CustomerDto cd =  modelMapper.map(savedCustomer, CustomerDto.class);
		cd.setUserDto(u); 
		return cd;	
	}

	@Override
	public SellerDto updateSeller(SellerDto sellerDto, Integer sellerId) {
		
		Seller seller = this.sellerRepo.findById(sellerId).orElseThrow(()-> new ResourceNotFoundException("User ","id ", sellerId));
		if(!checkAuthorization(seller.getUser().getEmail())) {
			throw new UnauthorizedResourceAccessException("Email", SecurityContextHolder.getContext().getAuthentication().getName());
		}
		
		Optional<User> findByEmail = userRepo.findByEmail(sellerDto.getEmail());
		if(findByEmail.isPresent() && !seller.getUser().getEmail().equalsIgnoreCase(sellerDto.getEmail())) {
			throw new DuplicateEntityInsertionException("Email", sellerDto.getEmail(), "Email already exists");
		}
		
		List<String> rolesFromSecurityContext = getRolesFromSecurityContext();
		for(String role:rolesFromSecurityContext) {
			if(role.equals("ROLE_ADMIN")) {
				seller.getUser().setActive(sellerDto.isActive());
			}
		}
		seller.setFirstName(sellerDto.getFirstName());
		seller.setLastName(sellerDto.getLastName());
		seller.setMobileNumber(sellerDto.getMobileNumber());
		seller.setDateOfBirth(sellerDto.getDateOfBirth());
		seller.getUser().setPassword(sellerDto.getPassword());	
		seller.getUser().setEmail(sellerDto.getEmail());
		
		Seller updatedSeller = sellerRepo.save(seller);
		
		SellerDto sd =  modelMapper.map(updatedSeller, SellerDto.class);
		sd.setRoles(updatedSeller.getUser().getRoles());
		sd.setEmail(updatedSeller.getUser().getEmail());
		sd.setRegistrationDate(updatedSeller.getUser().getRegistrationDate());
		sd.setActive(updatedSeller.getUser().isActive());
		return sd;	
	}


	@Override
	public CustomerDto updateCustomer(CustomerDto customerDto, Integer customerId) {
		Customer customer = this.customerRepo.findById(customerId).orElseThrow(()-> new ResourceNotFoundException("User ","id ", customerId));
		System.out.println(customerId);
		customer.setFirstName(customerDto.getFirstName());
		customer.setLastName(customerDto.getLastName());
		customer.setMobileNumber(customerDto.getMobileNumber());
		customer.setDateOfBirth(customerDto.getDateOfBirth());
		customer.getUser().setPassword(customerDto.getUserDto().getPassword());	
		customer.getUser().setEmail(customerDto.getUserDto().getEmail());
		Customer updatedCustomer = customerRepo.save(customer);
		UserDto u = modelMapper.map(updatedCustomer.getUser(), UserDto.class);
		CustomerDto cd =  modelMapper.map(updatedCustomer, CustomerDto.class);
		cd.setUserDto(u);
		return cd;
	}

	@Override
	public void deleteSeller(Integer sellerId) {
		Seller seller = this.sellerRepo.findById(sellerId).orElseThrow(()-> new ResourceNotFoundException("Seller","id ", sellerId));
		System.out.println(seller.getUser().getEmail());
		this.userRepo.delete(seller.getUser());
//		this.sellerRepo.delete(seller);	
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		Customer customer = this.customerRepo.findById(customerId).orElseThrow(()-> new ResourceNotFoundException("Customer","id ", customerId));
		this.customerRepo.delete(customer);	
	}

	@Override
	public SellerDto getSellerById(Integer sellerId) {
		Seller seller = this.sellerRepo.findById(sellerId).orElseThrow(()-> new ResourceNotFoundException("Seller","id ", sellerId));
		UserDto u = modelMapper.map(seller.getUser(), UserDto.class);
		SellerDto sd =  modelMapper.map(seller, SellerDto.class);
		sd.setUserId(u.getUserId());
		sd.setEmail(u.getEmail());
		sd.setRoles(u.getRoles());
		sd.setActive(u.isActive());
		sd.setRegistrationDate(u.getRegistrationDate());
		return sd;	
	}

	@Override
	public CustomerDto getCustomerById(Integer customerId) {
		Customer customer = this.customerRepo.findById(customerId).orElseThrow(()-> new ResourceNotFoundException("Customer","id ", customerId));
		UserDto u = modelMapper.map(customer.getUser(), UserDto.class);
		CustomerDto cd =  modelMapper.map(customer, CustomerDto.class);
		cd.setUserDto(u); 
		return cd;	
	}
	
	
	private static boolean checkAuthorization(String userEmail) {
		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		List<String> roles = new ArrayList<String>();
		authorities.forEach((a)->roles.add(a.getAuthority()));
		for(String role:roles) {
			if(role.equals("ROLE_ADMIN")) {
				return true;
			}
		}
		String contextEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		if(contextEmail.equalsIgnoreCase(userEmail)) {
			return true;
		}else {
			return false;
		}
		
	}
	
	private static List<String> getRolesFromSecurityContext() {
		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		List<String> roles = new ArrayList<String>();
		authorities.forEach((a)->roles.add(a.getAuthority()));
		return roles;
	}
	
	

}
