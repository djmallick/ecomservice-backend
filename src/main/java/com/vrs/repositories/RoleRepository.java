package com.vrs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vrs.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	Role findByRoleName(String roleName);

}
