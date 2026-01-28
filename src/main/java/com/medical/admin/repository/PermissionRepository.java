package com.medical.admin.repository;

import com.medical.admin.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;


@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
   
    Optional<Permission> findByPermissionName(String permissionname);
    
   
    boolean existsByPermissionName(String permissionname);
    
 
    List<Permission> findByCategory(String category);
    
    
    List<Permission> findByPermissionNameContaining(String keyword);
}