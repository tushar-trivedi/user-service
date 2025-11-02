package com.nexus.user_service.service;

import com.nexus.user_service.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    
    /**
     * Create a new user
     * @param name user's name
     * @param email user's email
     * @param password user's password (will be hashed)
     * @param roles user's roles
     * @return created User
     * @throws RuntimeException if email already exists
     */
    User createUser(String name, String email, String password, List<String> roles);
    
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
     * @param name new name (optional)
     * @param email new email (optional)
     * @return updated User
     * @throws RuntimeException if user not found or email already exists
     */
    User updateUser(String id, String name, String email);
    
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
