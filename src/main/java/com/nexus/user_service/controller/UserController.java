package com.nexus.user_service.controller;

import com.nexus.user_service.dto.request.UserCreateRequestDTO;
import com.nexus.user_service.dto.request.UserUpdateRequestDTO;
import com.nexus.user_service.dto.request.UserValidationRequestDTO;
import com.nexus.user_service.dto.response.UserResponseDTO;
import com.nexus.user_service.dto.response.UserListResponseDTO;
import com.nexus.user_service.model.User;
import com.nexus.user_service.service.UserService;
import com.nexus.user_service.utils.LoggerUtils;
import com.nexus.user_service.utils.ValidationUtils;
import com.nexus.user_service.utils.ResponseUtils;
import com.nexus.user_service.utils.MapperUtils;
import com.nexus.user_service.utils.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class UserController {
    
    private static final Logger logger = LoggerUtils.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    /**
     * Create a new user
     * POST /api/v1/users
     * Request: UserCreateRequestDTO
     * Response: UserResponseDTO
     */
    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserCreateRequestDTO request) {
        try {
            logger.info("Creating user with email: {}", request.getEmail());
            
            // Validate request
            String validationError = ValidationUtils.validateUserCreateRequest(request);
            if (validationError != null) {
                return ResponseEntity.badRequest().body(ResponseUtils.error(validationError));
            }
            
            // Validate email format
            if (!ValidationUtils.isValidEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body(ResponseUtils.error("Invalid email format"));
            }
            
            User user = userService.createUser(request);
            
            UserResponseDTO response = MapperUtils.toUserResponseDTO(user);
            logger.info("User created successfully: {}", user.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.success("User created successfully", response));
            
        } catch (RuntimeException e) {
            logger.error("Error creating user: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ResponseUtils.error(e.getMessage()));
        }
    }
    
    /**
     * Validate user credentials
     * POST /api/v1/auth/validate-user
     * Request: UserValidationRequestDTO
     * Response: UserResponseDTO or 401 Unauthorized
     */
    @PostMapping("/auth/validate-user")
    public ResponseEntity<Map<String, Object>> validateUser(@RequestBody UserValidationRequestDTO request) {
        try {
            logger.info("Validating user with email: {}", request.getEmail());
            
            // Validate request
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ResponseUtils.error("Email is required"));
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ResponseUtils.error("Password is required"));
            }
            
            // Validate email format
            if (!ValidationUtils.isValidEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body(ResponseUtils.error("Invalid email format"));
            }
            
            UserResponseDTO response = userService.validateUser(request);
            logger.info("User validation successful for: {}", request.getEmail());
            
            return ResponseEntity.ok(ResponseUtils.success("User validation successful", response));
            
        } catch (RuntimeException e) {
            logger.warn("User validation failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtils.unauthorized("Invalid credentials"));
        }
    }
    
    /**
     * Get all users
     * GET /api/v1/users
     * Response: List of UserListResponseDTO
     */
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        try {
            logger.info("Fetching all users");
            
            List<User> users = userService.getAllUsers();
            List<UserListResponseDTO> response = MapperUtils.toUserListResponseDTOs(users);
            
            logger.info("Retrieved {} users", users.size());
            return ResponseEntity.ok(ResponseUtils.success("Users retrieved successfully", response));
            
        } catch (RuntimeException e) {
            logger.error("Error retrieving users: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.error(e.getMessage()));
        }
    }
    
    /**
     * Get user by ID
     * GET /api/v1/users/{id}
     * Response: UserResponseDTO
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable String id) {
        try {
            logger.info("Fetching user by ID: {}", id);
            
            // Validate ID
            if (!ValidationUtils.isValidId(id)) {
                return ResponseEntity.badRequest().body(ResponseUtils.error("Invalid user ID format"));
            }
            
            Optional<User> userOpt = userService.getUserById(id);
            
            if (userOpt.isPresent()) {
                UserResponseDTO response = MapperUtils.toUserDetailResponseDTO(userOpt.get());
                logger.info("Retrieved user: {}", userOpt.get().getId());
                return ResponseEntity.ok(ResponseUtils.success("User retrieved successfully", response));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.notFound("User"));
            }
            
        } catch (RuntimeException e) {
            logger.error("Error retrieving user by ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.error(e.getMessage()));
        }
    }
    
    /**
     * Update user
     * PUT /api/v1/users/{id}
     * Request: UserUpdateRequestDTO
     * Response: UserResponseDTO
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable String id, 
            @RequestBody UserUpdateRequestDTO request) {
        try {
            logger.info("Updating user with ID: {}", id);
            
            // Validate ID
            if (!ValidationUtils.isValidId(id)) {
                return ResponseEntity.badRequest().body(ResponseUtils.error("Invalid user ID format"));
            }
            
            // Validate request
            String validationError = ValidationUtils.validateUserUpdateRequest(request);
            if (validationError != null) {
                return ResponseEntity.badRequest().body(ResponseUtils.error(validationError));
            }
            
            // Validate email format if provided
            if (request.getEmail() != null && !ValidationUtils.isValidEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body(ResponseUtils.error("Invalid email format"));
            }
            
            User updatedUser = userService.updateUser(id, request);
            UserResponseDTO response = MapperUtils.toUserResponseDTO(updatedUser);
            
            logger.info("User updated successfully: {}", updatedUser.getId());
            return ResponseEntity.ok(ResponseUtils.success("User updated successfully", response));
            
        } catch (ExceptionUtils.InsufficientFundsException e) {
            logger.warn("Insufficient funds for user {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(ResponseUtils.error(e.getMessage()));
        } catch (RuntimeException e) {
            logger.error("Error updating user: {}", e.getMessage());
            
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.notFound("User"));
            } else {
                return ResponseEntity.badRequest().body(ResponseUtils.error(e.getMessage()));
            }
        }
    }
    
    /**
     * Delete user
     * DELETE /api/v1/users/{id}
     * Response: { message: "User deleted successfully" }
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String id) {
        try {
            logger.info("Deleting user with ID: {}", id);
            
            // Validate ID
            if (!ValidationUtils.isValidId(id)) {
                return ResponseEntity.badRequest().body(ResponseUtils.error("Invalid user ID format"));
            }
            
            boolean deleted = userService.deleteUser(id);
            
            if (deleted) {
                logger.info("User deleted successfully: {}", id);
                return ResponseEntity.ok(ResponseUtils.deleted("User"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.error("Failed to delete user"));
            }
            
        } catch (RuntimeException e) {
            logger.error("Error deleting user: {}", e.getMessage());
            
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.notFound("User"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.error(e.getMessage()));
            }
        }
    }
    
    /**
     * Health check endpoint
     * GET /api/v1/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        logger.info("Health check requested");
        Map<String, Object> healthData = Map.of(
            "status", "UP",
            "service", "user-service",
            "timestamp", System.currentTimeMillis()
        );
        return ResponseEntity.ok(ResponseUtils.success("Service is healthy", healthData));
    }
}
