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
import com.vrs.dto.OrderCancellationRequestDto;
import com.vrs.dto.OrderDto;
import com.vrs.entities.Customer;
import com.vrs.entities.DeliveryAddress;
import com.vrs.entities.Order;
import com.vrs.entities.OrderCancellationRequest;
import com.vrs.entities.OrderStatus;
import com.vrs.entities.Product;
import com.vrs.entities.Seller;
import com.vrs.exception.InvalidUpdateException;
import com.vrs.exception.ProductOutOfStockException;
import com.vrs.exception.ResourceNotFoundException;
import com.vrs.payload.OrderPagedResponse;
import com.vrs.repositories.CustomerRepo;
import com.vrs.repositories.OrderCancellationRequestRepo;
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
	OrderCancellationRequestRepo orderCancellationRequestRepo;
	
	@Autowired
	ModelMapper modelMapper;
	

	@Override
	public OrderDto createOrder(OrderDto orderDto) {
		Product product = productRepo.findById(orderDto.getProductId()).orElseThrow(()-> new ResourceNotFoundException("Product","id", orderDto.getProductId()));
		Customer customer = customerRepo.findById(orderDto.getCustomerId()).orElseThrow(()-> new ResourceNotFoundException("Customer","id", orderDto.getCustomerId()));
		if(product.getStock()>0 && product.isActive()) {
			if(product.getStock()-orderDto.getQuantity()>0) {
				Order order = new Order();
				DeliveryAddress address = modelMapper.map(orderDto.getDeliveryAddress(), DeliveryAddress.class);
				order.setActive(true);
				order.setOrderDate(new Date());
				order.setStatus(OrderStatus.PLACED);
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
		if(order.getStatus().equals(OrderStatus.PLACED)||order.getStatus().equals(OrderStatus.ACCEPTED)) {
			Instant start = order.getOrderDate().toInstant();
			Instant stop = new Date().toInstant();
			Instant target = start.plus( 48 , ChronoUnit.HOURS );
			if(!stop.isAfter( target )) {
				changeAddress(orderDto.getDeliveryAddress(), order.getDeliveryAddress());
			} else {
				throw new InvalidUpdateException("Order address for ", orderId,"Address can be updated till 48 hours");
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

		if(orderDto.getStatus()!= null && !(order.getStatus().equals((OrderStatus.DELIVERED))||order.getStatus().equals((OrderStatus.CANCELLED)))) {
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
				if(order.getStatus().equals((OrderStatus.CANCEL_REQUESTED))) {
					OrderCancellationRequest orderCancellationRequest = orderCancellationRequestRepo.findByOrder(order);
					orderCancellationRequest.setDateOfReview(new Date());
					orderCancellationRequest.setCancelled(true);
					orderCancellationRequest.setActive(false);
					orderCancellationRequestRepo.save(orderCancellationRequest);
				}
				order.setStatus(OrderStatus.CANCELLED);
				int newStock = order.getProduct().getStock()+order.getQuantity();
				order.getProduct().setStock(newStock);
				order.setActive(false);
			}else if(status.equals("requestedCancelled")) {
				order.setStatus(OrderStatus.CANCEL_REQUESTED);
				order.setActive(true);
			} else {
				throw new InvalidUpdateException("Order status for ID ", orderId,"status is invalid"); 
			}
		} else {
			throw new InvalidUpdateException("Order status for ID ", orderId,"status is either null or already delivered or cancelled"); 
		}
		
		Order updatedOrder = orderRepo.save(order);
		
		OrderDto updatedOrderDto = orderToOrderDto(updatedOrder);
		updatedOrderDto.setSellerName(updatedOrder.getProduct().getSeller().getFirstName()+" "+updatedOrder.getProduct().getSeller().getLastName());
		updatedOrderDto.setSellerMobile(updatedOrder.getProduct().getSeller().getMobileNumber());
		DeliveryAddressDto addressDto = modelMapper.map(updatedOrder.getDeliveryAddress(), DeliveryAddressDto.class);
		updatedOrderDto.setDeliveryAddress(addressDto);
		return updatedOrderDto;
		
	}
	

	//Order cancellation rule ->
	//Customer will try to cancel the order
	//If status is placed, order will be cancelled automatically
	//If status is acceped, seller will review the cancellation request and will cancel it
	//Order cannot be cancelled if status is shipped or delivered. 
	//Order once cancelled, status can not be changed again
	
	@Override
	public OrderCancellationRequestDto requestForCancelOrder(OrderCancellationRequestDto request, Integer orderId) {
		Order order = orderRepo.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order","id", orderId));
		OrderDto orderDto = new OrderDto();
		if(order.getStatus().equals((OrderStatus.PLACED))) {
			OrderCancellationRequest cancellationRequest = new OrderCancellationRequest();
			cancellationRequest.setActive(false);
			cancellationRequest.setDateOfRequest(new Date());
			cancellationRequest.setDateOfReview(cancellationRequest.getDateOfRequest());
			cancellationRequest.setCancelled(true);
			cancellationRequest.setReasonOfCancellation(request.getReasonOfCancellation());
			cancellationRequest.setOrder(order);
			OrderCancellationRequest orderCancellationRequest = orderCancellationRequestRepo.save(cancellationRequest);
			orderDto.setStatus("cancelled");
			updateOrderStatus(orderDto, orderId);
			return modelMapper.map(orderCancellationRequest, OrderCancellationRequestDto.class);
			
		} else if(order.getStatus().equals((OrderStatus.ACCEPTED))) {
			OrderCancellationRequest cancellationRequest = new OrderCancellationRequest();
			cancellationRequest.setActive(true);
			cancellationRequest.setDateOfRequest(new Date());
			cancellationRequest.setOrder(order);
			OrderCancellationRequest orderCancellationRequest = orderCancellationRequestRepo.save(cancellationRequest);
			cancellationRequest.setReasonOfCancellation(request.getReasonOfCancellation());
			orderDto.setStatus("requestedCancelled");
			updateOrderStatus(orderDto, orderId);
			return modelMapper.map(orderCancellationRequest, OrderCancellationRequestDto.class);
		}else {
			throw new InvalidUpdateException("Order status for ID ", orderId,"status should be placed or accepted for cancellation"); 
		}
		
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
		
		Pageable p = getPageable(pageNumber, pageSize, sortBy, sortDir);
		Page<Order> pageOrders = orderRepo.findAll(p);
		
		return createOrderPagedResponse(pageOrders);
	}

	@Override
	public OrderPagedResponse getOrdersByCustomerId(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, Integer customerId, Boolean active) {
		Customer customer = customerRepo.findById(customerId).orElseThrow(()-> new ResourceNotFoundException("Customer","id", customerId));
		
		Pageable p = getPageable(pageNumber, pageSize, sortBy, sortDir);
		Page<Order> pageOrders;
		if(active==null) {
			pageOrders = orderRepo.findByCustomer(customer, p);
		}else {
			pageOrders = orderRepo.findByCustomerActiveOrder(customer, active.booleanValue(), p);
		}
		return createOrderPagedResponse(pageOrders);
	}


	@Override
	public OrderPagedResponse getOrdersBySellerId(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
			Integer sellerId, Boolean active) {
		Seller seller = sellerRepo.findById(sellerId).orElseThrow(()-> new ResourceNotFoundException("Seller","id", sellerId));
		
		Pageable p = getPageable(pageNumber, pageSize, sortBy, sortDir);
		Page<Order> pageOrders;
		if(active==null) {
			pageOrders = orderRepo.findBySeller(seller, p);
		}else {
			pageOrders = orderRepo.findBySellerActiveOrder(seller, active.booleanValue(), p);
		}

		return createOrderPagedResponse(pageOrders);
	}


	@Override
	public OrderPagedResponse getOrdersByProductId(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,
			Integer productId, Boolean active) {
		Product product = productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product","id", productId));
		
		Pageable p = getPageable(pageNumber, pageSize, sortBy, sortDir);
		Page<Order> pageOrders;
		if(active==null) {
			pageOrders = orderRepo.findByProduct(product, p);
		}else {
			System.out.println("===================>"+active.booleanValue());
			pageOrders = orderRepo.findByProductActiveOrder(product, active.booleanValue(), p);
		}

		return createOrderPagedResponse(pageOrders);
	}
	

	private OrderDto orderToOrderDto(Order order) {
		OrderDto dto = this.modelMapper.map(order, OrderDto.class);
		dto.setCustomerId(order.getCustomer().getCustomer_Id());
		dto.setProductId(order.getProduct().getProductId());
		return dto;
	}
	
	public OrderPagedResponse createOrderPagedResponse(Page<Order> pageOrders) {
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
		OrderPagedResponse orderPagedResponse = new OrderPagedResponse();
		orderPagedResponse.setContents(fetchedOrders);
		orderPagedResponse.setPageNumber(pageOrders.getNumber());
		orderPagedResponse.setPageSize(pageOrders.getSize());
		orderPagedResponse.setTotalElements(pageOrders.getTotalElements());
		orderPagedResponse.setLastPage(pageOrders.isLast());
		orderPagedResponse.setTotalPages(pageOrders.getTotalPages());
		orderPagedResponse.setSuccessful(true);
		orderPagedResponse.setMessage("Fetched successfully");
		return orderPagedResponse;
	}
	
	public Pageable getPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
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
