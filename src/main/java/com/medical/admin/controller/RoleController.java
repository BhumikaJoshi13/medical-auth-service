package com.medical.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medical.admin.entity.Permission;
import com.medical.admin.entity.Role;
import com.medical.admin.repository.PermissionRepository;
import com.medical.admin.repository.RoleRepository;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

 
 
    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        try {
            // Check if role already exists
            if (roleRepository.findByRoleName(role.getRoleName()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Role with name '" + role.getRoleName() + "' already exists");
            }
            
            Role savedRole = roleRepository.save(role);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating role: " + e.getMessage());
        }
    }
    // ✅ Add permission to role
    @PostMapping("/{roleId}/permissions/{permissionId}")
    public Role addPermissionToRole(
            @PathVariable Long roleId,
            @PathVariable Long permissionId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        role.getPermissions().add(permission);
        return roleRepository.save(role);
    }
}

