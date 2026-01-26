
package com.medical.admin.controller;

import com.medical.admin.dto.UserCreateRequest;
import com.medical.admin.dto.UserResponse;
import com.medical.admin.dto.UserUpdateRequest;
import com.medical.admin.entity.Role;
import com.medical.admin.entity.User;
import com.medical.admin.service.UserManagementService;

import com.medical.admin.repository.RoleRepository;
import com.medical.admin.repository.UserRepository;

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
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	/**
	 * Constructor-based dependency injection.
	 */
	@Autowired
	public UserManagementController(UserManagementService userManagementService) {
		this.userManagementService = userManagementService;
	}

	// ==================== CREATE OPERATIONS ====================

	// Create a new user.

	@PostMapping
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
		UserResponse response = userManagementService.createUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// ==================== READ OPERATIONS ====================

	public UserResponse getUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
		return new UserResponse(user);
	}

	public UserResponse getUserByUsername(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found with username: " + username));
		return new UserResponse(user);
	}

	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(UserResponse::new).toList();
	}

	public List<UserResponse> getUsersByRole(String roleName) {
		Role role = roleRepository.findByRoleName(roleName)
				.orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

		return userRepository.findByRoles(role).stream().map(UserResponse::new).toList();
	}

	public List<UserResponse> getActiveUsers() {
		return userRepository.findByIsActive(true).stream().map(UserResponse::new).toList();
	}

	public List<UserResponse> getVerifiedUsers() {
		return userRepository.findByIsEmailVerified(true).stream().map(UserResponse::new).toList();
	}

	public List<UserResponse> searchUsersByName(String name) {
		return userRepository.findByFirstNameContainingIgnoreCaseOrLastnameContainingIgnoreCase(name, name).stream()
				.map(UserResponse::new).toList();
	}

	// ==================== UPDATE OPERATIONS ====================

	// Update user details.
	@PutMapping("/{id}")
	public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
			@Valid @RequestBody UserUpdateRequest request) {
		UserResponse response = userManagementService.updateUser(id, request);
		return ResponseEntity.ok(response);
	}

	// Verify user's email.
	@PatchMapping("/{id}/verify-email")
	public ResponseEntity<Map<String, String>> verifyEmail(@PathVariable Long id) {
		userManagementService.verifyEmail(id);
		return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
	}

	// Deactivate user account (soft delete).
	@PatchMapping("/{id}/deactivate")
	public ResponseEntity<Map<String, String>> deactivateUser(@PathVariable Long id) {
		userManagementService.deactivateUser(id);
		return ResponseEntity.ok(Map.of("message", "User deactivated successfully"));
	}

	// Activate user account.

	@PatchMapping("/{id}/activate")
	public ResponseEntity<Map<String, String>> activateUser(@PathVariable Long id) {
		userManagementService.activateUser(id);
		return ResponseEntity.ok(Map.of("message", "User activated successfully"));
	}

	// ==================== DELETE OPERATIONS ====================

	// Permanently delete user (hard delete).
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
		userManagementService.deleteUser(id);
		return ResponseEntity.ok(Map.of("message", "User deleted permanently"));
	}

	// ==================== STATISTICS ====================

	// Get user statistics.

	@GetMapping("/stats")
	public ResponseEntity<Map<String, Object>> getUserStats() {
		Map<String, Object> stats = new HashMap<>();
		stats.put("totalUsers", userManagementService.getAllUsers().size());
		stats.put("totalDoctors", userManagementService.getUserCountByRole("DOCTOR"));
		stats.put("totalPatients", userManagementService.getUserCountByRole("PATIENT"));
		stats.put("totalReceptionists", userManagementService.getUserCountByRole("RECEPTIONIST"));
		stats.put("totalPharmacists", userManagementService.getUserCountByRole("PHARMACIST"));
		stats.put("totalAdmins", userManagementService.getUserCountByRole("ADMIN"));
		stats.put("activeUsers", userManagementService.getActiveUsersCount());
		stats.put("verifiedUsers", userManagementService.getVerifiedUsersCount());

		return ResponseEntity.ok(stats);
	}
}