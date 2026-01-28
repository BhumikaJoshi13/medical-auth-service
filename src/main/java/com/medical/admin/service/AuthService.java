package com.medical.admin.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.medical.admin.entity.Role;
import com.medical.admin.entity.User;
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
    private JWTUtil jwtUtil;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    // Register user
    public Map<String, Object> register(User user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        System.out.println(" Register called with username: " + user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(savedUser.getUsername());

        String token = jwtUtil.generateToken(userDetails);

        System.out.println("User saved with ID: " + savedUser.getUserId());

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
