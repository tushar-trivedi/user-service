package com.nexus.user_service.dto.request;

import java.util.List;

public class UserBatchRequestDTO {
    
    private List<String> userIds;
    
    // Default constructor
    public UserBatchRequestDTO() {}
    
    // Constructor with userIds
    public UserBatchRequestDTO(List<String> userIds) {
        this.userIds = userIds;
    }
    
    // Getters and Setters
    public List<String> getUserIds() {
        return userIds;
    }
    
    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
    
    @Override
    public String toString() {
        return "UserBatchRequestDTO{" +
                "userIds=" + userIds +
                '}';
    }
}
