package com.vrs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vrs.entities.Complain;

public interface ComplainRepo extends JpaRepository<Complain, Integer> {

}
