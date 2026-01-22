package com.medical.admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medical.admin.entity.Users;
import com.medical.admin.entity.Users.Role;

import java.util.List;
public interface UserRepository  extends JpaRepository<Users, Long>{
	 Optional<Users> findByUsername(String username) ;
	
	Optional<Users> findByEmail(String email);
	
	Boolean existsByUsername(String username);
	
	Boolean existsByEmail(String email);
	 List<Users> findByRole(Role role);
	List<Users> findByIsActive(Boolean isActive);
	  List<Users> findByRoleAndIsActive(Role role, Boolean isActive);
	
	 List<Users> findByIsEmailVerified(Boolean isEmailVerified);

	 long countByRole(Role role);

	 Optional<Users> searchByName(String name);
}
