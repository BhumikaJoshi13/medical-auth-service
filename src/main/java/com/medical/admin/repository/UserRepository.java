package com.medical.admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medical.admin.entity.User;
import com.medical.admin.entity.Role;

import java.util.List;
public interface UserRepository  extends JpaRepository<User, Long>{
	 Optional<User> findByUsername(String username) ;
	
	Optional<User> findByEmail(String email);
	
	Boolean existsByUsername(String username);
	
	Boolean existsByEmail(String email);
	 List<User> findByRoles(Role role);
	List<User> findByIsActive(Boolean isActive);
	  List<User> findByRolesAndIsActive(Role role, Boolean isActive);
	
	 List<User> findByIsEmailVerified(Boolean isEmailVerified);

	 long countByRoles(Role role);

	 
	

	 List<User> findByFirstNameContainingIgnoreCaseOrLastnameContainingIgnoreCase(
	         String firstName, String lastName
	 );

	
}
