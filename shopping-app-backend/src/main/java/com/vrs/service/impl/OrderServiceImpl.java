package com.vrs.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vrs.dto.DeliveryAddressDto;
import com.vrs.dto.OrderDto;
import com.vrs.entities.Customer;
import com.vrs.entities.DeliveryAddress;
import com.vrs.entities.Order;
import com.vrs.entities.Product;
import com.vrs.exception.InvalidUpdateException;
import com.vrs.exception.ProductOutOfStockException;
import com.vrs.exception.ResourceNotFoundException;
import com.vrs.repositories.CategoryRepo;
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
				order.setStatus("placed");
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
	public OrderDto updateOrder(OrderDto orderDto, Integer orderId) {
		Order order = orderRepo.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order","id", orderId));
		
		if(order.isActive()) {
			Instant start = order.getOrderDate().toInstant();
			Instant stop = new Date().toInstant();
			Instant target = start.plus( 24 , ChronoUnit.HOURS );
			if(!stop.isAfter( target )) {
				updateOrderAddress(orderDto.getDeliveryAddress(), order.getDeliveryAddress());
			} else {
				throw new InvalidUpdateException("Order address for ", orderId,"Address can be updated till 24 hours");
			}
		} else {
			throw new InvalidUpdateException("Order address for ", orderId,"order is not active");
		}
		
		Order updatedOrder = orderRepo.save(order);
		
		OrderDto updatedOrderDto = orderToOrderDto(updatedOrder);
		updatedOrderDto.setSellerName(updatedOrder.getProduct().getSeller().getFirstName()+" "+updatedOrder.getProduct().getSeller().getLastName());
		updatedOrderDto.setSellerMobile(updatedOrder.getProduct().getSeller().getMobileNumber());
		DeliveryAddressDto addressDto = modelMapper.map(updatedOrder.getDeliveryAddress(), DeliveryAddressDto.class);
		updatedOrderDto.setDeliveryAddress(addressDto);
		return updatedOrderDto;
	}
	
	
	private void updateOrderAddress(DeliveryAddressDto newAddress, DeliveryAddress currentAddress) {
		currentAddress.setAddress1(newAddress.getAddress1());
		currentAddress.setAddress2(newAddress.getAddress2());
		currentAddress.setDistrict(newAddress.getDistrict());
		currentAddress.setMobileNumber(newAddress.getMobileNumber());
		currentAddress.setState(newAddress.getState());
		currentAddress.setPinCode(newAddress.getPinCode());
	}

	@Override
	public boolean deleteOrder(Integer orderId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public OrderDto getOrderById(Integer orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderDto> getAllOrders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderDto> getOrdersByCustomerId(Integer customerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderDto> getOrdersByCustomerId(Integer customerId, boolean isActive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderDto> getOrdersBySellerId(Integer sellerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderDto> getOrdersBySellerId(Integer sellerId, boolean isActive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderDto> getOrdersByProductId(Integer productId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Order orderDtoToOrder(OrderDto orderDto) {
		return this.modelMapper.map(orderDto, Order.class);
	}

	private OrderDto orderToOrderDto(Order order) {
		OrderDto dto = this.modelMapper.map(order, OrderDto.class);
		dto.setCustomerId(order.getCustomer().getCustomer_Id());
		dto.setProductId(order.getProduct().getProductId());
		return dto;
	}


}
