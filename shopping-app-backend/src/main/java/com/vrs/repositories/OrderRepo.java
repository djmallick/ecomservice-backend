package com.vrs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vrs.entities.Order;

public interface OrderRepo extends JpaRepository<Order, Integer> {

}
