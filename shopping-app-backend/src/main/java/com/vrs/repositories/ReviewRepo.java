package com.vrs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vrs.entities.Review;

public interface ReviewRepo extends JpaRepository<Review, Integer> {

}
