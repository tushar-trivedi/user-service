package com.nexus.user_service.dto.request;

import java.math.BigDecimal;

public class UserUpdateRequestDTO {
    
    private String name;
    private String email;
    private BigDecimal walletBalance; // Optional field for wallet updates
    
    // Default constructor
    public UserUpdateRequestDTO() {}
    
    // Constructor with name and email
    public UserUpdateRequestDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    // Constructor with all fields
    public UserUpdateRequestDTO(String name, String email, BigDecimal walletBalance) {
        this.name = name;
        this.email = email;
        this.walletBalance = walletBalance;
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
    
    public BigDecimal getWalletBalance() {
        return walletBalance;
    }
    
    public void setWalletBalance(BigDecimal walletBalance) {
        this.walletBalance = walletBalance;
    }
    
    @Override
    public String toString() {
        return "UserUpdateRequestDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", walletBalance=" + walletBalance +
                '}';
    }
}
