package com.vrs.service.impl;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vrs.dto.OrderDto;
import com.vrs.entities.Customer;
import com.vrs.entities.Order;
import com.vrs.entities.Product;
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
	public OrderDto createOrder(Integer productId, Integer customerId) {
		Product product = productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product","id", productId));
		Customer customer = customerRepo.findById(customerId).orElseThrow(()-> new ResourceNotFoundException("Customer","id", customerId));
		if(product.getStock()>0) {
			Order order = new Order();
			order.setActive(true);
			order.setOrderDate(new Date());
			order.setStatus("placed");
			order.setProduct(product);
			order.setCustomer(customer);
			Order createdOrder = orderRepo.save(order);
			product.setStock(product.getStock()-1);
			productRepo.save(product);
			return orderToOrderDto(createdOrder);
		} else {
			throw new ProductOutOfStockException(productId);
		}
	}


	@Override
	public OrderDto updateOrder(OrderDto orderDto, Integer orderId) {
		// TODO Auto-generated method stub
		return null;
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
