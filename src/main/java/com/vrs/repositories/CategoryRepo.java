package com.vrs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vrs.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

}
