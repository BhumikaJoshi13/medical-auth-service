package com.medical.admin.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.medical.admin.dto.UserCreateRequest;
import com.medical.admin.entity.Role;
import com.medical.admin.entity.User;
import com.medical.admin.repository.RoleRepository;
import com.medical.admin.repository.UserRepository;
import com.medical.admin.security.JWTUtil;

import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository rolerepository;
    
    @Autowired
    private JWTUtil jwtUtil;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    // Register user
    @Transactional
    public Map<String, Object> register(UserCreateRequest request) {
        
        // Validation
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        System.out.println("🔵 Register called with username: " + request.getUsername());
        
        // Create User entity
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastname(request.getLastname());
        user.setPhone(request.getPhone());
        
        // Get role from request
        String roleName = request.getRole();
        
        System.out.println("🔍 Role from request: " + roleName);
        
        if (roleName == null || roleName.trim().isEmpty()) {
            roleName = "PATIENT"; // Default role
            System.out.println("⚠️ No role provided, using default: " + roleName);
        }
        
        final String finalRoleName = roleName;
        // Fetch the Role entity from database
        Role userRole = rolerepository.findByRoleName(roleName)
            .orElseThrow(() -> new RuntimeException("Role '" + finalRoleName+ "' not found in database. Please ensure the role exists in the 'role' table."));
        
        System.out.println(" Fetched role from DB: " + userRole.getRoleName() + " (ID: " + userRole.getRoleId() + ")");
        
        // Assign the role to user
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        
        System.out.println("📝 Roles assigned to user before save: " + 
            user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()));
        
        // Save user (this will automatically populate user_role table due to @ManyToMany)
        User savedUser = userRepository.save(user);
        
        System.out.println(" User saved with ID: " + savedUser.getUserId());
        System.out.println("Roles after save: " + 
            savedUser.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()));
        
        // Load user details for JWT
        UserDetails userDetails = 
                customUserDetailsService.loadUserByUsername(savedUser.getUsername());
        
        // Generate token
        String token = jwtUtil.generateToken(userDetails);
        
        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("token", token);
        response.put("userId", savedUser.getUserId());
        response.put("username", savedUser.getUsername());
        response.put("email", savedUser.getEmail());
        response.put("roles",
                savedUser.getRoles()
                         .stream()
                         .map(Role::getRoleName)
                         .toList()
        );
        
        return response;
    }
    //Login 
    public Map<String, Object> login(User request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("Account is inactive");
        }

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(user.getUsername());

        String token = jwtUtil.generateToken(userDetails);


        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("token", token);
        response.put("userId", user.getUserId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("roles",
                user.getRoles()
                    .stream()
                    .map(Role::getRoleName)
                    .toList());

        return response;
    }
    
    public Map<String, String> logout(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid or missing token");
        }

        String token = authHeader.substring(7);

        

        return Map.of("message", "Logout successful");
    }

}
