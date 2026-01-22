
package com.medical.admin.controller;

import com.medical.admin.dto.UserCreateRequest;
import com.medical.admin.dto.UserResponse;
import com.medical.admin.dto.UserUpdateRequest;
import com.medical.admin.entity.Users.Role;
import com.medical.admin.service.UserManagementService;
import com.medical.admin.exception.InvalidRequestException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for User Management operations.
 */
@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserManagementController {
    
    /**
     * Service layer dependency.
     */
    private final UserManagementService userManagementService;
    
    /**
     * Constructor-based dependency injection.
     */
    @Autowired
    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }
    
    // ==================== CREATE OPERATIONS ====================
    
   
     //Create a new user.
    
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = userManagementService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    
    // ==================== READ OPERATIONS ====================
    
    //Get user by ID.
   
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse response = userManagementService.getUserById(id);
        return ResponseEntity.ok(response);
    }
    
    //Get user by username.
     
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        UserResponse response = userManagementService.getUserByUsername(username);
        return ResponseEntity.ok(response);
    }
    
     //Get all users.
        @GetMapping
       public ResponseEntity<List<UserResponse>> getAllUsers() {
           List<UserResponse> users = userManagementService.getAllUsers();
            return ResponseEntity.ok(users);
        }
    
    
      //Get users by role.
  
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponse>> getUsersByRole(@PathVariable String role) {
        try {
            Role userRole = Role.valueOf(role.toUpperCase());
            List<UserResponse> users = userManagementService.getUsersByRole(userRole);
            return ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException(
                "Invalid role: " + role + 
                ". Valid roles: ADMIN, DOCTOR, PATIENT, RECEPTIONIST, PHARMACIST"
            );
        }
    }
    
    // Get only active users.
    @GetMapping("/active")
    public ResponseEntity<List<UserResponse>> getActiveUsers() {
        List<UserResponse> users = userManagementService.getActiveUsers();
        return ResponseEntity.ok(users);
    }
    
   
    // Get only email-verified users.
    @GetMapping("/verified")
    public ResponseEntity<List<UserResponse>> getVerifiedUsers() {
        List<UserResponse> users = userManagementService.getVerifiedUsers();
        return ResponseEntity.ok(users);
    }
    
    //Search users by name.
    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam String name) {
        List<UserResponse> users = userManagementService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }
    
    // ==================== UPDATE OPERATIONS ====================
    
    //Update user details. 
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userManagementService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }
    
   //Verify user's email.
    @PatchMapping("/{id}/verify-email")
    public ResponseEntity<Map<String, String>> verifyEmail(@PathVariable Long id) {
        userManagementService.verifyEmail(id);
        return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
    }
    
    //Deactivate user account (soft delete).  
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Map<String, String>> deactivateUser(@PathVariable Long id) {
        userManagementService.deactivateUser(id);
        return ResponseEntity.ok(Map.of("message", "User deactivated successfully"));
    }
   
     //Activate user account.
     
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Map<String, String>> activateUser(@PathVariable Long id) {
        userManagementService.activateUser(id);
        return ResponseEntity.ok(Map.of("message", "User activated successfully"));
    }
    
    // ==================== DELETE OPERATIONS ====================
    
   //Permanently delete user (hard delete). 
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userManagementService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted permanently"));
    }
    
    // ==================== STATISTICS ====================
    
    //Get user statistics.
     
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUserStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userManagementService.getAllUsers().size());
        stats.put("totalDoctors", userManagementService.getUserCountByRole(Role.DOCTOR));
        stats.put("totalPatients", userManagementService.getUserCountByRole(Role.PATIENT));
        stats.put("totalReceptionists", userManagementService.getUserCountByRole(Role.RECEPTIONIST));
        stats.put("totalPharmacists", userManagementService.getUserCountByRole(Role.PHARMACIST));
        stats.put("totalAdmins", userManagementService.getUserCountByRole(Role.ADMIN));
        stats.put("activeUsers", userManagementService.getActiveUsersCount());
        stats.put("verifiedUsers", userManagementService.getVerifiedUsersCount());
        
        return ResponseEntity.ok(stats);
    }
}