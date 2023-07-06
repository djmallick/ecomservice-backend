package com.vrs.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

import com.vrs.dto.DeliveryAddressDto;
import com.vrs.dto.OrderDto;
import com.vrs.entities.Customer;
import com.vrs.entities.DeliveryAddress;
import com.vrs.entities.Order;
import com.vrs.entities.OrderStatus;
import com.vrs.entities.Product;
import com.vrs.exception.InvalidUpdateException;
import com.vrs.exception.ProductOutOfStockException;
import com.vrs.exception.ResourceNotFoundException;
import com.vrs.payload.OrderPagedResponse;
import com.vrs.repositories.CustomerRepo;
import com.vrs.repositories.OrderRepo;
import com.vrs.repositories.ProductRepo;
import com.vrs.repositories.SellerRepo;
import com.vrs.repositories.UserRepo;
import com.vrs.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	
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
	ModelMapper modelMapper;
	

	@Override
	public OrderDto createOrder(OrderDto orderDto) {
		Product product = productRepo.findById(orderDto.getProductId()).orElseThrow(()-> new ResourceNotFoundException("Product","id", orderDto.getProductId()));
		Customer customer = customerRepo.findById(orderDto.getCustomerId()).orElseThrow(()-> new ResourceNotFoundException("Customer","id", orderDto.getCustomerId()));
		if(product.getStock()>0) {
			if(product.getStock()-orderDto.getQuantity()>0) {
				Order order = new Order();
				DeliveryAddress address = modelMapper.map(orderDto.getDeliveryAddress(), DeliveryAddress.class);
				order.setActive(true);
				order.setOrderDate(new Date());
				order.setStatus(OrderStatus.ACCEPTED);
				order.setDeliveryAddress(address);
				order.setProduct(product);
				order.setCustomer(customer);
				order.setQuantity(orderDto.getQuantity());
				Order createdOrder = orderRepo.save(order);
				product.setStock(product.getStock()-orderDto.getQuantity());
				productRepo.save(product);
				
				OrderDto createdOrderDto = orderToOrderDto(createdOrder);
				createdOrderDto.setSellerName(createdOrder.getProduct().getSeller().getFirstName()+" "+createdOrder.getProduct().getSeller().getLastName());
				createdOrderDto.setSellerMobile(createdOrder.getProduct().getSeller().getMobileNumber());
				DeliveryAddressDto addressDto = modelMapper.map(createdOrder.getDeliveryAddress(), DeliveryAddressDto.class);
				createdOrderDto.setDeliveryAddress(addressDto);
				return createdOrderDto;
			} else {
				throw new ProductOutOfStockException(orderDto.getProductId(),"Quantity greater than stock!");
			}
			
		} else {
			throw new ProductOutOfStockException(orderDto.getProductId(), "Out of stock!");
		}
	}


	@Override
	public OrderDto updateOrderAddress(OrderDto orderDto, Integer orderId) {
		Order order = orderRepo.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order","id", orderId));
		
		if(order.getStatus().equals(OrderStatus.ACCEPTED)) {
			Instant start = order.getOrderDate().toInstant();
			Instant stop = new Date().toInstant();
			Instant target = start.plus( 24 , ChronoUnit.HOURS );
			if(!stop.isAfter( target )) {
				changeAddress(orderDto.getDeliveryAddress(), order.getDeliveryAddress());
			} else {
				throw new InvalidUpdateException("Order address for ", orderId,"Address can be updated till 24 hours");
			}
		} else {
			throw new InvalidUpdateException("Order address for ", orderId,"order is already shipped or delivered");
		}
		
		Order updatedOrder = orderRepo.save(order);
		
		OrderDto updatedOrderDto = orderToOrderDto(updatedOrder);
		updatedOrderDto.setSellerName(updatedOrder.getProduct().getSeller().getFirstName()+" "+updatedOrder.getProduct().getSeller().getLastName());
		updatedOrderDto.setSellerMobile(updatedOrder.getProduct().getSeller().getMobileNumber());
		DeliveryAddressDto addressDto = modelMapper.map(updatedOrder.getDeliveryAddress(), DeliveryAddressDto.class);
		updatedOrderDto.setDeliveryAddress(addressDto);
		return updatedOrderDto;
	}
	
	
	private void changeAddress(DeliveryAddressDto newAddress, DeliveryAddress currentAddress) {
		currentAddress.setAddress1(newAddress.getAddress1());
		currentAddress.setAddress2(newAddress.getAddress2());
		currentAddress.setDistrict(newAddress.getDistrict());
		currentAddress.setMobileNumber(newAddress.getMobileNumber());
		currentAddress.setState(newAddress.getState());
		currentAddress.setPinCode(newAddress.getPinCode());
	}
	
	@Override
	public OrderDto updateOrderStatus(OrderDto orderDto, Integer orderId) {
		Order order = orderRepo.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order","id", orderId));
		//Updating order status by seller
		if(orderDto.getStatus()!= null) {
			String status = orderDto.getStatus();
			if(status.equals("accepted")) {
				order.setStatus(OrderStatus.ACCEPTED);
				order.setActive(true);
			} else if(status.equals("shipped")) {
				order.setStatus(OrderStatus.SHIPPED);
				order.setActive(true);
			} else if(status.equals("delivered")) {
				order.setStatus(OrderStatus.DELIVERED);
				order.setActive(false);
			} else if(status.equals("cancelled")) {
				order.setStatus(OrderStatus.CANCELLED);
				order.setActive(false);
			} else {
				throw new InvalidUpdateException("Order status for ID ", orderId,"status is invalid"); 
			}
		} else {
			throw new InvalidUpdateException("Order status for ID ", orderId,"status should be not null"); 
		}
		
		Order updatedOrder = orderRepo.save(order);
		
		OrderDto updatedOrderDto = orderToOrderDto(updatedOrder);
		updatedOrderDto.setSellerName(updatedOrder.getProduct().getSeller().getFirstName()+" "+updatedOrder.getProduct().getSeller().getLastName());
		updatedOrderDto.setSellerMobile(updatedOrder.getProduct().getSeller().getMobileNumber());
		DeliveryAddressDto addressDto = modelMapper.map(updatedOrder.getDeliveryAddress(), DeliveryAddressDto.class);
		updatedOrderDto.setDeliveryAddress(addressDto);
		return updatedOrderDto;
		
	}


	@Override
	public boolean deleteOrder(Integer orderId) {
		Order order = orderRepo.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order","id", orderId));
		orderRepo.delete(order);
		Optional<Order> deletedOrder = orderRepo.findById(orderId);
		return deletedOrder.isEmpty();
	}

	@Override
	public OrderDto getOrderById(Integer orderId) {
		Order order = orderRepo.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order","id", orderId));
		OrderDto fetchedOrderDto = orderToOrderDto(order);
		DeliveryAddressDto addressDto = modelMapper.map(order.getDeliveryAddress(), DeliveryAddressDto.class);
		fetchedOrderDto.setDeliveryAddress(addressDto);
		fetchedOrderDto.setSellerName(order.getProduct().getSeller().getFirstName()+" "+order.getProduct().getSeller().getLastName());
		fetchedOrderDto.setSellerMobile(order.getProduct().getSeller().getMobileNumber());
		return fetchedOrderDto;
	}

	@Override
	public OrderPagedResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		Sort sort = null;
		if(sortDir.equals("desc")) {
			sort = Sort.by(sortBy).descending();
		}
		else {
			sort = Sort.by(sortBy).ascending();
		}
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<Order> pageOrders = orderRepo.findAll(p);
		List<Order> orders = pageOrders.getContent();
		List<OrderDto> fetchedOrders = new ArrayList<OrderDto>();
		for(Order order:orders) {
			OrderDto fetchedOrderDto = orderToOrderDto(order);
			DeliveryAddressDto addressDto = modelMapper.map(order.getDeliveryAddress(), DeliveryAddressDto.class);
			fetchedOrderDto.setDeliveryAddress(addressDto);
			fetchedOrderDto.setSellerName(order.getProduct().getSeller().getFirstName()+" "+order.getProduct().getSeller().getLastName());
			fetchedOrderDto.setSellerMobile(order.getProduct().getSeller().getMobileNumber());
			fetchedOrders.add(fetchedOrderDto);
		}
		return createOrderPagedResponse(fetchedOrders, pageOrders);
	}

	@Override
	public OrderPagedResponse getOrdersByCustomerId(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, Integer customerId, Boolean active) {
		Customer customer = customerRepo.findById(customerId).orElseThrow(()-> new ResourceNotFoundException("Customer","id", customerId));
		Sort sort = null;
		if(sortDir.equals("desc")) {
			sort = Sort.by(sortBy).descending();
		}
		else {
			sort = Sort.by(sortBy).ascending();
		}
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<Order> pageOrders = null;
		if(active==null) {
			pageOrders = orderRepo.findByCustomer(customer, p);
		}else {
			pageOrders = orderRepo.findByCustomerActiveOrder(customer, active.booleanValue(), p);
		}

		List<Order> orders = pageOrders.getContent();
		List<OrderDto> fetchedOrders = new ArrayList<OrderDto>();
		for(Order order:orders) {
			OrderDto fetchedOrderDto = orderToOrderDto(order);
			DeliveryAddressDto addressDto = modelMapper.map(order.getDeliveryAddress(), DeliveryAddressDto.class);
			fetchedOrderDto.setDeliveryAddress(addressDto);
			fetchedOrderDto.setSellerName(order.getProduct().getSeller().getFirstName()+" "+order.getProduct().getSeller().getLastName());
			fetchedOrderDto.setSellerMobile(order.getProduct().getSeller().getMobileNumber());
			fetchedOrders.add(fetchedOrderDto);
		}
		return createOrderPagedResponse(fetchedOrders, pageOrders);
	}


	@Override
	public OrderPagedResponse getOrdersBySellerId(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
			Integer customerId, Boolean active) {
		
		return null;
	}


	@Override
	public List<OrderDto> getOrdersByProductId(Integer productId) {
		// TODO Auto-generated method stub
		return null;
	}
	

	private OrderDto orderToOrderDto(Order order) {
		OrderDto dto = this.modelMapper.map(order, OrderDto.class);
		dto.setCustomerId(order.getCustomer().getCustomer_Id());
		dto.setProductId(order.getProduct().getProductId());
		return dto;
	}
	
	public static OrderPagedResponse createOrderPagedResponse(List<OrderDto> orders, Page<Order> pageOrders) {
		OrderPagedResponse orderPagedResponse = new OrderPagedResponse();
		orderPagedResponse.setContents(orders);
		orderPagedResponse.setPageNumber(pageOrders.getNumber());
		orderPagedResponse.setPageSize(pageOrders.getSize());
		orderPagedResponse.setTotalElements(pageOrders.getTotalElements());
		orderPagedResponse.setLastPage(pageOrders.isLast());
		orderPagedResponse.setTotalPages(pageOrders.getTotalPages());
		orderPagedResponse.setSuccessful(true);
		orderPagedResponse.setMessage("Fetched successfully");
		return orderPagedResponse;
	}




}
