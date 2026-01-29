package com.medical.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medical.admin.dto.UserCreateRequest;
import com.medical.admin.entity.User;
import com.medical.admin.service.AuthService;



@RestController
@RequestMapping("/auth")
public class AuthController {
     
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserCreateRequest user) {
    	System.out.println(" REGISTER API HIT");
        System.out.println("User received: " + user.getUsername());
        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        return ResponseEntity.ok(authService.login(user));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestHeader("Authorization") String authHeader) {

        return ResponseEntity.ok(authService.logout(authHeader));
    }

}
