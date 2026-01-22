package com.medical.admin.service;

import com.medical.admin.dto.UserCreateRequest;
import com.medical.admin.dto.UserResponse;
import com.medical.admin.dto.UserUpdateRequest;
import com.medical.admin.entity.Users;
import com.medical.admin.entity.Users.Role;
import com.medical.admin.repository.UserRepository;
import com.medical.admin.exception.UserNotFoundException;
import com.medical.admin.exception.DuplicateResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManagementService {
	
	private final UserRepository userRepository;
	 private final PasswordEncoder passwordEncoder;
	 
	 
	 @Autowired
	    public UserManagementService(UserRepository userRepository, 
	                                 PasswordEncoder passwordEncoder) {
	        this.userRepository = userRepository;
	        this.passwordEncoder = passwordEncoder;
	    }
	 
	 
	 @Transactional
	    public UserResponse createUser(UserCreateRequest request) {
	        
	        // STEP 1: Validation - Check for duplicate username
	       
	        if (userRepository.existsByUsername(request.getUsername())) {
	            throw new DuplicateResourceException("username", request.getUsername());
	        }
	        
	        // STEP 2: Validation - Check for duplicate email
	        if (userRepository.existsByEmail(request.getEmail())) {
	            throw new DuplicateResourceException("email", request.getEmail());
	        }
	        
	        // STEP 3: Transform DTO → Entity
	        // Create new Users entity from request data
	        Users user = new Users();
	        user.setUsername(request.getUsername());
	        user.setEmail(request.getEmail());
	        
	        // STEP 4: Security - Hash password
	        
	        user.setPassword(passwordEncoder.encode(request.getPassword()));
	        
	        // STEP 5: Set remaining fields
	        user.setFirstName(request.getFirstName());
	        user.setLastname(request.getLastname());
	        user.setRole(request.getRole());
	        user.setPhone(request.getPhone());
	        user.setIsActive(request.getIsActive());
	        user.setIsEmailVerified(request.getIsEmailVerified());
	        
	        // STEP 6: Persist to database
	        
	        Users savedUser = userRepository.save(user);
	        
	        // STEP 7: Transform Entity → Response DTO
	       
	        return new UserResponse(savedUser);
	    }
	 
	 public UserResponse getUserById(Long userId) {
	       
	        Users user = userRepository.findById(userId)
	                .orElseThrow(() -> new UserNotFoundException(userId));
	        
	        return new UserResponse(user);
	    }
	 public UserResponse getUserByUsername(String username) {
	        Users user = userRepository.findByUsername(username)
	                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
	        
	        return new UserResponse(user);
	    }
	    
	 public List<UserResponse> getUsersByRole(Role role) {
	        return userRepository.findByRole(role).stream()
	                .map(UserResponse::new)
	                .collect(Collectors.toList());
	    }
	 
	 public List<UserResponse> getActiveUsers() {
	        return userRepository.findByIsActive(true).stream()
	                .map(UserResponse::new)
	                .collect(Collectors.toList());
	    }
	 
	 public List<UserResponse> getAllUsers() {
	        return userRepository.findAll().stream()
	                .map(UserResponse::new)
	                .collect(Collectors.toList());
	    }
	 
	    public List<UserResponse> getVerifiedUsers() {
	        return userRepository.findByIsEmailVerified(true).stream()
	                .map(UserResponse::new)
	                .collect(Collectors.toList());
	    }
	    
	    public List<UserResponse> searchUsersByName(String name) {
	        return userRepository.searchByName(name).stream()
	                .map(UserResponse::new)
	                .collect(Collectors.toList());
	    }
	    
	    
	    //Updating user details 
	    @Transactional
	    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
	        
	        // STEP 1: Fetch existing user from database
	    	 Users user = userRepository.findById(userId)
	                 .orElseThrow(() -> new UserNotFoundException(userId));
	        // STEP 2: Update email if provided
	        
	    	 if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
	             if (userRepository.existsByEmail(request.getEmail())) {
	                 throw new DuplicateResourceException("email", request.getEmail());
	             }
	             user.setEmail(request.getEmail());
	         }
	        // STEP 3: Update password if provided
	       
	        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
	            // Always hash new password before storing
	            user.setPassword(passwordEncoder.encode(request.getPassword()));
	        }
	        
	        // STEP 4: Update first name if provided
	        if (request.getFirstName() != null) {
	            user.setFirstName(request.getFirstName());
	        }
	        
	        // STEP 5: Update last name if provided
	        if (request.getLastname() != null) {
	            user.setLastname(request.getLastname());
	        }
	        
	        // STEP 6: Update role if provided
	       
	        if (request.getRole() != null) {
	            user.setRole(request.getRole());
	        }
	        
	        // STEP 7: Update phone if provided
	        if (request.getPhone() != null) {
	            user.setPhone(request.getPhone());
	        }
	        
	        // STEP 8: Update active status if provided
	       
	        if (request.getIsActive() != null) {
	            user.setIsActive(request.getIsActive());
	        }
	        
	        // STEP 9: Update email verification status if provided
	        
	        if (request.getIsEmailVerified() != null) {
	            user.setIsEmailVerified(request.getIsEmailVerified());
	        }
	        
	        // STEP 10: Save updated entity to database
	        // @UpdateTimestamp on Users entity automatically updates 'updatedAt'
	        Users updatedUser = userRepository.save(user);
	        
	        // STEP 11: Return updated data (without password)
	        return new UserResponse(updatedUser);
	    }
	    
	
	    @Transactional
	    public void updateLastLogin(String username) {
	        Users user = userRepository.findByUsername(username)
	                .orElseThrow(() -> new RuntimeException("User not found"));
	        
	        // Set current timestamp
	        user.setLastLogin(LocalDateTime.now());
	        userRepository.save(user);
	    }
	    
	    
	    @Transactional
	    public void verifyEmail(Long userId) {
	        Users user = userRepository.findById(userId)
	                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
	        
	        user.setIsEmailVerified(true);
	        userRepository.save(user);
	    }
	    
	    
	    @Transactional
	    public void deactivateUser(Long userId) {
	        Users user = userRepository.findById(userId)
	                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
	        
	        user.setIsActive(false);
	        userRepository.save(user);
	    }
	
	    @Transactional
	    public void activateUser(Long userId) {
	        Users user = userRepository.findById(userId)
	                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
	        
	        user.setIsActive(true);
	        userRepository.save(user);
	    }
	    
	   
	    @Transactional
	    public void deleteUser(Long userId) {
	        // Check if user exists before deleting
	        
	        if (!userRepository.existsById(userId)) {
	            throw new RuntimeException("User not found with ID: " + userId);
	        }
	        
	        userRepository.deleteById(userId);
	    }
	    
	    
	     //Get count of users by role.
	   
	    public long getUserCountByRole(Role role) {
	        return userRepository.countByRole(role);
	    }
	    
	    
	     //Get count of active users.
	     
	    public long getActiveUsersCount() {
	        return userRepository.findByIsActive(true).size();
	    }
	    
	    /**
	    //Get count of verified users.
	     * 
	     * USE CASE: Track email verification progress
	     * "Verified: 150 / Total: 200 (75%)"
	     * 
	     */
	    public long getVerifiedUsersCount() {
	        return userRepository.findByIsEmailVerified(true).size();
	    }
}
