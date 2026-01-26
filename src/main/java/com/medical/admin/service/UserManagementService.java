package com.medical.admin.service;

import com.medical.admin.dto.UserCreateRequest;
import com.medical.admin.dto.UserResponse;
import com.medical.admin.dto.UserUpdateRequest;
import com.medical.admin.entity.Role;
import com.medical.admin.entity.User;
import com.medical.admin.exception.DuplicateResourceException;
import com.medical.admin.repository.RoleRepository;
import com.medical.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class UserManagementService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserManagementService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    
    
    private Role getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() ->
                        new RuntimeException("Role not found: " + roleName)
                );
    }

 // ================= CREATE USER =================

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("username", request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("email", request.getEmail());
        }

        Role role = getRoleByName(request.getRole());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastname(request.getLastname());
        user.setPhone(request.getPhone());
        user.setIsActive(request.getIsActive());
        user.setIsEmailVerified(request.getIsEmailVerified());
        user.setRoles(Set.of(role));

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }
    


    // ================= UPDATE USER =================

    @Transactional
    public UserResponse updateUser(Long userId, UserUpdateRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastname() != null) {
            user.setLastname(request.getLastname());
        }

        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        if (request.getIsActive() != null) {
            user.setIsActive(request.getIsActive());
        }

        if (request.getIsEmailVerified() != null) {
            user.setIsEmailVerified(request.getIsEmailVerified());
        }

//        if (request.getRole() != null) {
//            user.setRoles(Set.of(getRoleByName(request.getRole())));
//        }



        User updatedUser = userRepository.save(user);
        return new UserResponse(updatedUser);
    }

    // ================= STATUS / FLAGS =================

    @Transactional
    public void updateLastLogin(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void verifyEmail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        user.setIsEmailVerified(true);
        userRepository.save(user);
    }

    @Transactional
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Transactional
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        user.setIsActive(true);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    // ================= COUNTS =================

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public long getUserCountByRole(String roleName) {
        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        return userRepository.countByRoles(role);
    }

    public long getActiveUsersCount() {
        return userRepository.findByIsActive(true).size();
    }

    public long getVerifiedUsersCount() {
        return userRepository.findByIsEmailVerified(true).size();
    }
}
