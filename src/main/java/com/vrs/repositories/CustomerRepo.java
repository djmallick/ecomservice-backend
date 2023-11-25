package com.vrs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vrs.entities.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {

}
