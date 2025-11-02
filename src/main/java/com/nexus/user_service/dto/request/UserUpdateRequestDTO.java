package com.nexus.user_service.dto.request;

public class UserUpdateRequestDTO {
    
    private String name;
    private String email;
    
    // Default constructor
    public UserUpdateRequestDTO() {}
    
    // Constructor with all fields
    public UserUpdateRequestDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String toString() {
        return "UserUpdateRequestDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
