package com.medical.admin.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Table(name = "permissions")
public class Permission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private int permissionId;
    

    @Column(name = "permission_name" ,unique = true, nullable = false, length = 100)
    private String permissionName;
    

   
    @Column(length = 255)
    private String description;
    
   
    @Column(length = 50)
    private String category;
    
 
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Permission() {}
    
    public Permission(String permissionName, String description, String category) {
        this.permissionName = permissionName;
        this.description = description;
        this.category = category;
    }
    
    // Getters and Setters
    public int getPermissionId() {
        return permissionId;
    }
    
    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }
    
    public String getName() {
        return permissionName;
    }
    
    public void setName(String permissionname) {
        this.permissionName = permissionname;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Set<Role> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}