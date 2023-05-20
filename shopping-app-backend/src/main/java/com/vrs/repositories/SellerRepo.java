package com.vrs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vrs.entities.Seller;

public interface SellerRepo extends JpaRepository<Seller, Integer> {

}
