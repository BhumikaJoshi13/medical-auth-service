package com.medical.admin.dto;

import com.medical.admin.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserUpdateRequest {
   
    @Email(message = "Email should be valid")
    private String email;
  
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;
    
 
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastname;
    
    private Role role;
    
  
    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    private String phone;
    
    private Boolean isActive;
    
 
    private Boolean isEmailVerified;
    
 
    public UserUpdateRequest() {}
    
   
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastname() {
        return lastname;
    }
    
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Boolean getIsEmailVerified() {
        return isEmailVerified;
    }
    
    public void setIsEmailVerified(Boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }
}