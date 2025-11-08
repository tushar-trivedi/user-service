# User Service

A Spring Boot microservice for user management and validation in a microservice architecture.

## Project Structure

```
user-service/
├── pom.xml                     # Maven dependencies and build configuration
├── src/main/java/com/nexus/user_service/
│   ├── UserServiceApplication.java           # Spring Boot main application
│   ├── controller/
│   │   └── UserController.java              # REST API endpoints (/api/v1)
│   ├── service/
│   │   ├── UserService.java                 # Service interface
│   │   └── UserServiceImpl.java             # Service implementation
│   ├── repository/
│   │   └── UserRepository.java              # MongoDB data access layer
│   ├── model/
│   │   └── User.java                        # User entity/document model
│   ├── dto/
│   │   ├── request/                         # Request DTOs
│   │   │   ├── UserCreateRequestDTO.java
│   │   │   ├── UserUpdateRequestDTO.java
│   │   │   └── UserValidationRequestDTO.java
│   │   └── response/                        # Response DTOs
│   │       ├── UserResponseDTO.java
│   │       └── UserListResponseDTO.java
│   └── utils/
│       ├── LoggerUtils.java                 # Centralized logging utility
│       ├── MapperUtils.java                 # DTO mapping utility
│       ├── PasswordUtils.java               # SHA-256 password hashing
│       ├── ResponseUtils.java               # API response formatting
│       └── ValidationUtils.java             # Input validation utility
├── src/main/resources/
│   └── application.properties               # MongoDB and logging configuration
└── src/test/java/                          # Unit tests
```

## Core Features

- **User Management**: Complete CRUD operations for user entities
- **User Validation**: Service-to-service credential validation for microservice authentication
- **MongoDB Integration**: Document-based user storage with Spring Data MongoDB
- **REST API**: Clean RESTful endpoints with proper HTTP status codes
- **DTO Pattern**: Request/response DTOs for API security and data transfer
- **Layered Architecture**: Controller → Service → Repository pattern
- **Utility Classes**: Reusable utilities for logging, validation, mapping, and password hashing

## API Endpoints

### User Management

#### Create User
- **POST** `/api/v1/users`
- **Description**: Create a new user account
- **Request Body**:
```json
{
  "firstName": "string",
  "lastName": "string", 
  "email": "string",
  "password": "string"
}
```
- **Response**: `201 Created`
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": "string",
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "createdAt": "timestamp",
    "updatedAt": "timestamp"
  }
}
```
- **Error Responses**: `400 Bad Request` (validation errors, duplicate email)

#### Get All Users
- **GET** `/api/v1/users`
- **Description**: Retrieve all users (limited fields)
- **Response**: `200 OK`
```json
{
  "success": true,
  "message": "Users retrieved successfully",
  "data": [
    {
      "id": "string",
      "firstName": "string", 
      "lastName": "string",
      "email": "string"
    }
  ]
}
```

#### Get User by ID
- **GET** `/api/v1/users/{id}`
- **Description**: Retrieve a specific user by ID
- **Response**: `200 OK`
```json
{
  "success": true,
  "message": "User retrieved successfully",
  "data": {
    "id": "string",
    "firstName": "string",
    "lastName": "string", 
    "email": "string",
    "createdAt": "timestamp",
    "updatedAt": "timestamp"
  }
}
```
- **Error Responses**: `404 Not Found`, `400 Bad Request` (invalid ID format)

#### Update User
- **PUT** `/api/v1/users/{id}`
- **Description**: Update user information
- **Request Body**:
```json
{
  "firstName": "string",
  "lastName": "string",
  "email": "string"
}
```
- **Response**: `200 OK`
```json
{
  "success": true,
  "message": "User updated successfully", 
  "data": {
    "id": "string",
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "createdAt": "timestamp",
    "updatedAt": "timestamp"
  }
}
```
- **Error Responses**: `404 Not Found`, `400 Bad Request` (validation errors)

#### Delete User
- **DELETE** `/api/v1/users/{id}`
- **Description**: Delete a user account
- **Response**: `200 OK`
```json
{
  "success": true,
  "message": "User deleted successfully"
}
```
- **Error Responses**: `404 Not Found`, `400 Bad Request` (invalid ID format)

#### Validate User
- **POST** `/api/v1/auth/validate-user`
- **Description**: Validate user credentials for service-to-service authentication
- **Request Body**:
```json
{
  "email": "string",
  "password": "string"
}
```
- **Response**: `200 OK`
```json
{
  "success": true,
  "message": "User validation successful",
  "data": {
    "id": "string",
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "createdAt": "timestamp",
    "updatedAt": "timestamp"
  }
}
```
- **Error Responses**: `401 Unauthorized` (invalid credentials), `400 Bad Request` (validation errors)


#### Health Check
- **GET** `/api/v1/health`
- **Description**: Service health status
- **Response**: `200 OK`
```json
{
  "success": true,
  "message": "Service is healthy",
  "data": {
    "status": "UP",
    "service": "user-service", 
    "timestamp": 1699123456789
  }
}
```

## Local Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MongoDB (installed via Homebrew)

### Installation Steps

1. **Install MongoDB using Homebrew**
   ```bash
   # Install Homebrew (if not already installed)
   /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
   
   # Install MongoDB
   brew tap mongodb/brew
   brew install mongodb-community
   
   # Start MongoDB service
   brew services start mongodb/brew/mongodb-community
   ```

2. **Clone and Setup Project**
   ```bash
   # Navigate to project directory
   cd user-service
   
   # Install dependencies
   mvn clean install
   ```

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```
   The application will start on `http://localhost:3000`

4. **Verify Setup**
   - MongoDB will automatically create the `nexus_users` database
   - Application logs will show successful MongoDB connection
   - Check logs for "Started UserServiceApplication" message

## Configuration

### MongoDB Configuration
Located in `src/main/resources/application.properties`:
```properties
# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/database_name

# Logging Configuration
logging.level.com.nexus.user_service=DEBUG
logging.level.org.springframework.data.mongodb=DEBUG
```

### For Deployed MongoDB
To connect to a deployed MongoDB instance (MongoDB Atlas):
1. Update the `spring.data.mongodb.uri` in `application.properties`
2. Use the MongoDB Atlas connection string format:
   ```properties
   spring.data.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/?appName=ClusterName/database_name
   ```
   
**Example Atlas URI format:**
```properties
spring.data.mongodb.uri=mongodb+srv://nexus:nexus5@cluster0.kei0rsa.mongodb.net/?appName=Cluster0/nexus_users
```

**Note:** Replace `username`, `password`, `cluster.mongodb.net`, `ClusterName`, and `database_name` with your actual MongoDB Atlas credentials and cluster information.

## Microservice Architecture

This service is designed as part of a microservice ecosystem:
- **User Management**: Handles user CRUD operations
- **User Validation**: Provides credential validation for other services
- **Service Separation**: Authentication logic is handled by separate auth service
- **Clean API**: RESTful endpoints with consistent response formats
- **Stateless**: No session management, suitable for distributed systems
