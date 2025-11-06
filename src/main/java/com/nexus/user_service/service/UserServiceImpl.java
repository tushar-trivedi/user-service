package com.nexus.user_service.service;

import com.nexus.user_service.dto.request.UserCreateRequestDTO;
import com.nexus.user_service.dto.request.UserUpdateRequestDTO;
import com.nexus.user_service.model.User;
import com.nexus.user_service.repository.UserRepository;
import com.nexus.user_service.utils.LoggerUtils;
import com.nexus.user_service.utils.MapperUtils;
import com.nexus.user_service.utils.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    
    private static final Logger logger = LoggerUtils.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public User createUser(UserCreateRequestDTO request) {
        logger.info("Creating user with email: {}", request.getEmail());
        
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User with email " + request.getEmail() + " already exists");
        }
        
        // Use MapperUtils to convert DTO to User entity
        User user = MapperUtils.toUser(request);
        user.setPasswordHash(PasswordUtils.hashPassword(request.getPassword()));
        user.setVerified(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        logger.info("User created successfully with ID: {}", savedUser.getId());
        
        return savedUser;
    }
    
    @Override
    public User authenticateUser(String email, String password) {
        logger.info("Authenticating user with email: {}", email);
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
                logger.info("Authentication successful for user: {}", email);
                return user;
            }
        }
        
        logger.warn("Authentication failed for user: {}", email);
        return null;
    }
    
    @Override
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll();
    }
    
    @Override
    public Optional<User> getUserById(String id) {
        logger.info("Fetching user by ID: {}", id);
        return userRepository.findById(id);
    }
    
    @Override
    public Optional<User> getUserByEmail(String email) {
        logger.info("Fetching user by email: {}", email);
        return userRepository.findByEmail(email);
    }
    
    @Override
    public User updateUser(String id, UserUpdateRequestDTO request) {
        logger.info("Updating user with ID: {}", id);
        
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User with ID " + id + " not found");
        }
        
        User user = userOpt.get();
        
        // Check if new email is already taken by another user
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email " + request.getEmail() + " is already taken");
            }
        }
        
        // Use MapperUtils to update user from DTO
        MapperUtils.updateUserFromDTO(user, request);
        user.setUpdatedAt(LocalDateTime.now());
        
        User updatedUser = userRepository.save(user);
        logger.info("User updated successfully: {}", updatedUser.getId());
        
        return updatedUser;
    }
    
    @Override
    public boolean deleteUser(String id) {
        logger.info("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User with ID " + id + " not found");
        }
        
        userRepository.deleteById(id);
        logger.info("User deleted successfully: {}", id);
        
        return true;
    }
    
    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public User verifyUser(String id) {
        logger.info("Verifying user with ID: {}", id);
        
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User with ID " + id + " not found");
        }
        
        User user = userOpt.get();
        user.setVerified(true);
        user.setUpdatedAt(LocalDateTime.now());
        
        User verifiedUser = userRepository.save(user);
        logger.info("User verified successfully: {}", verifiedUser.getId());
        
        return verifiedUser;
    }
    
    @Override
    public List<User> getUsersByRole(String role) {
        logger.info("Fetching users by role: {}", role);
        return userRepository.findByRole(role);
    }
}
