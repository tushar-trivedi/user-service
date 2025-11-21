package com.nexus.user_service.dto.response;

import java.util.List;

public class UserBatchResponseDTO {
    private String id;
    private String email;
    private List<String> roles;

    // Default constructor
    public UserBatchResponseDTO() {}

    // Constructor with all fields
    public UserBatchResponseDTO(String id, String email, List<String> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserBatchResponseDTO{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}
