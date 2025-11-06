package com.nexus.user_service.service;

import com.nexus.user_service.dto.request.UserCreateRequestDTO;
import com.nexus.user_service.dto.request.UserUpdateRequestDTO;
import com.nexus.user_service.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    
    /**
     * Create a new user
     * @param request UserCreateRequestDTO containing user data
     * @return created User
     * @throws RuntimeException if email already exists
     */
    User createUser(UserCreateRequestDTO request);
    
    /**
     * Authenticate user with email and password
     * @param email user's email
     * @param password user's password
     * @return User if authentication successful, null otherwise
     */
    User authenticateUser(String email, String password);
    
    /**
     * Get all users
     * @return List of all users
     */
    List<User> getAllUsers();
    
    /**
     * Get user by ID
     * @param id user's ID
     * @return Optional<User>
     */
    Optional<User> getUserById(String id);
    
    /**
     * Get user by email
     * @param email user's email
     * @return Optional<User>
     */
    Optional<User> getUserByEmail(String email);
    
    /**
     * Update user information
     * @param id user's ID
     * @param request UserUpdateRequestDTO containing updated data
     * @return updated User
     * @throws RuntimeException if user not found or email already exists
     */
    User updateUser(String id, UserUpdateRequestDTO request);
    
    /**
     * Delete user by ID
     * @param id user's ID
     * @return true if deleted successfully
     * @throws RuntimeException if user not found
     */
    boolean deleteUser(String id);
    
    /**
     * Check if user exists by email
     * @param email user's email
     * @return true if user exists
     */
    boolean userExistsByEmail(String email);
    
    /**
     * Verify user account
     * @param id user's ID
     * @return updated User
     * @throws RuntimeException if user not found
     */
    User verifyUser(String id);
    
    /**
     * Get users by role
     * @param role the role to search for
     * @return List<User>
     */
    List<User> getUsersByRole(String role);
    
}
