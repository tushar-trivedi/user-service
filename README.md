# User Service

A Spring Boot microservice for user management

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
│   │   │   └── LoginRequestDTO.java
│   │   └── response/                        # Response DTOs
│   │       ├── UserResponseDTO.java
│   │       ├── UserListResponseDTO.java
│   │       └── LoginResponseDTO.java
│   └── utils/
│       ├── LoggerUtils.java                 # Centralized logging utility
│       ├── MapperUtils.java                 # DTO mapping utility
│       ├── PasswordUtils.java               # SHA-256 password hashing
│       ├── ResponseUtils.java               # API response formatting
│       └── ValidationUtils.java             # Input validation utility
├── src/main/resources/
│   └── application.properties               # MongoDB and logging configuration
├── src/test/java/                          # Unit tests
└── bruno-user-service/                     # Bruno API collection for testing
    ├── bruno.json                          # Bruno configuration
    ├── Create User John.bru                # Create user test
    ├── Get All Users.bru                   # Get users test
    ├── Get User by ID.bru                  # Get single user test
    ├── Update User.bru                     # Update user test
    ├── Delete User.bru                     # Delete user test
    └── Login User.bru                      # Login test
```

### Core Features
- **User Management**: Create, read, update, delete users
- **Authentication**: User login with password hashing (SHA-256)
- **MongoDB Integration**: Document-based user storage
- **REST API**: Clean RESTful endpoints with proper HTTP status codes
- **DTO Pattern**: Request/response DTOs for API security
- **Layered Architecture**: Controller → Service → Repository pattern
- **Utility Classes**: Reusable utilities for logging, validation, mapping, etc.


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
To connect to a deployed MongoDB instance:
1. Update the `spring.data.mongodb.uri` in `application.properties`
2. Replace `localhost:27017` with your deployed instance host and port
3. Add authentication credentials if required:
   ```properties
   spring.data.mongodb.uri=mongodb://username:password@host:port/database_name
   ```
