package com.nexus.user_service.dto.response;

public class LoginResponseDTO {
    
    private String token;
    private String userId;
    
    // Default constructor
    public LoginResponseDTO() {}
    
    // Constructor with all fields
    public LoginResponseDTO(String token, String userId) {
        this.token = token;
        this.userId = userId;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    @Override
    public String toString() {
        return "LoginResponseDTO{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
