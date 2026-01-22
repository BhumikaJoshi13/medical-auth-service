package com.medical.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medical.admin.entity.Users;
import com.medical.admin.repository.UserRepository;
import com.medical.admin.security.JWTUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JWTUtil jwtUtil;
    
    // ⭐ Register with Token
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user) {
        // Check username
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Username already exists"));
        }
        
        // Check email
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email already exists"));
        }
        
        // Encode password and save user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Users savedUser = userRepository.save(user);
        
        // ⭐ Generate JWT token
        String token = jwtUtil.generateToken(savedUser.getUsername());
        
        // Return response with token
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("token", token);
        response.put("userId", savedUser.getUserId());
        response.put("username", savedUser.getUsername());
        response.put("email", savedUser.getEmail());
        response.put("role", savedUser.getRole().name());
        
        return ResponseEntity.ok(response);
    }
    
    // ⭐ Login with Token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users request) {
        // Find user
        Users user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid credentials"));
        }
        
        // Check if user is active
        if (!user.getIsActive()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Account is inactive"));
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());
        
        // Return response with token
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("token", token);
        response.put("userId", user.getUserId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("role", user.getRole().name());
        
        return ResponseEntity.ok(response);
    }
}
