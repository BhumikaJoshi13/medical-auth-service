package com.medical.admin.repository;

import com.medical.admin.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
  
	Optional<Role> findByRoleName(String roleName);
    
    
    boolean existsByRoleName(String roleName);
 
    @Query("SELECT r FROM Role r JOIN r.permissions p WHERE p.permissionName = :permissionName")
    Set<Role> findByPermissionName(@Param("permissionName") String permissionName);
}
