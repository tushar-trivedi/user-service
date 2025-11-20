# User Service Integration Guide

Complete API reference for developers integrating with the User Service microservice.

## Base URL & Authentication

- **Base URL**: `http://localhost:3000/api/v1`
- **Authentication**: Currently open endpoints (authentication handled by API Gateway)
- **Content-Type**: `application/json`
- **Response Format**: JSON with standardized structure

## API Quick Reference Table

| Purpose | Method | Endpoint | Status Codes |
|---------|--------|----------|--------------|
| [Create new user account](#create-user) | POST | `/user` | 201, 400 |
| [Get all users (list view)](#get-all-users) | GET | `/users` | 200, 500 |
| [Get user by ID (detailed)](#get-user-by-id) | GET | `/users/{id}` | 200, 400, 404 |
| [Update user information](#update-user) | PUT | `/users/{id}` | 200, 400, 404 |
| [Delete user account](#delete-user) | DELETE | `/users/{id}` | 200, 400, 404, 500 |
| [Validate user credentials](#validate-user) | POST | `/auth/user/validate` | 200, 400, 401 |
| [Service health check](#health-check) | GET | `/health` | 200 |

---

## API Endpoints

### Create User

**POST** `/api/v1/user`

Create a new user account with roles and optional wallet balance.

#### Request
```http
POST /api/v1/user
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@example.com", 
  "password": "securepassword123",
  "roles": ["SUPPLIER", "FUNDER"],
  "walletBalance": 1000.00
}
```

#### Request Fields
- `name` (string, required): User's full name (2-50 characters, letters/spaces/hyphens)
- `email` (string, required): Valid email address (unique in system)
- `password` (string, required): Password (6-100 characters) 
- `roles` (array, required): One or more of: SUPPLIER, FUNDER, INVESTOR, ADMIN
- `walletBalance` (number, optional): Initial wallet balance (defaults to 0.00)

#### Success Response (201 Created)
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": "674c8b3d1234567890abcdef",
    "name": "John Doe",
    "email": "john.doe@example.com",
    "roles": ["SUPPLIER", "FUNDER"],
    "verified": false,
    "walletBalance": 1000.00,
    "fundingRequestIds": [],
    "createdAt": "2025-11-19T20:15:30.123",
    "updatedAt": "2025-11-19T20:15:30.123"
  },
  "timestamp": "2025-11-19T20:15:30.456"
}
```

#### Error Responses
```json
// 400 Bad Request - Validation Error
{
  "success": false,
  "error": "Invalid email format",
  "timestamp": "2025-11-19T20:15:30.456"
}

// 400 Bad Request - Duplicate Email  
{
  "success": false,
  "error": "User already exists with email: john.doe@example.com",
  "timestamp": "2025-11-19T20:15:30.456"
}
```


---

### Get All Users

**GET** `/api/v1/users`

Retrieve all users with limited fields (list view for administrative purposes).

#### Request
```http
GET /api/v1/users
```

#### Success Response (200 OK)
```json
{
  "success": true,
  "message": "Users retrieved successfully", 
  "data": [
    {
      "id": "674c8b3d1234567890abcdef",
      "name": "John Doe",
      "roles": ["SUPPLIER", "FUNDER"],
      "walletBalance": 1000.00
    },
    {
      "id": "674c8b3d1234567890abcd12",
      "name": "Alice Johnson", 
      "roles": ["INVESTOR"],
      "walletBalance": 5000.00
    }
  ],
  "timestamp": "2025-11-19T20:20:15.789"
}
```

#### Error Response
```json
// 500 Internal Server Error
{
  "success": false,
  "error": "Database connection error",
  "timestamp": "2025-11-19T20:20:15.789"
}
```


---

### Get User by ID

**GET** `/api/v1/users/{id}`

Retrieve detailed information for a specific user.

#### Request
```http
GET /api/v1/users/674c8b3d1234567890abcdef
```

#### URL Parameters
- `id` (string, required): MongoDB ObjectId (24 hex characters)

#### Success Response (200 OK)
```json
{
  "success": true,
  "message": "User retrieved successfully",
  "data": {
    "id": "674c8b3d1234567890abcdef", 
    "name": "John Doe",
    "email": "john.doe@example.com",
    "roles": ["SUPPLIER", "FUNDER"],
    "verified": false,
    "walletBalance": 1000.00,
    "fundingRequestIds": ["fund-001", "fund-002"],
    "createdAt": "2025-11-19T20:15:30.123",
    "updatedAt": "2025-11-19T20:25:45.678"
  },
  "timestamp": "2025-11-19T20:30:12.345"
}
```

#### Error Responses
```json
// 400 Bad Request - Invalid ID Format
{
  "success": false,
  "error": "Invalid user ID format", 
  "timestamp": "2025-11-19T20:30:12.345"
}

// 404 Not Found
{
  "success": false,
  "error": "User not found",
  "timestamp": "2025-11-19T20:30:12.345"
}
```


---

### Update User

**PUT** `/api/v1/users/{id}`

Update user information including wallet operations and funding request management.

#### Request Types

##### Basic Information Update
```http
PUT /api/v1/users/674c8b3d1234567890abcdef
Content-Type: application/json

{
  "name": "John Smith",
  "email": "john.smith@example.com"
}
```

##### Direct Wallet Balance Set
```http  
PUT /api/v1/users/674c8b3d1234567890abcdef
Content-Type: application/json

{
  "walletBalance": 2500.00
}
```

##### Wallet Adjustment (Add/Deduct)
```http
PUT /api/v1/users/674c8b3d1234567890abcdef
Content-Type: application/json

{
  "walletAdjustment": 500.00   // Add $500
}

{
  "walletAdjustment": -200.00  // Deduct $200
}
```

##### Funding Request ID Management
```http
PUT /api/v1/users/674c8b3d1234567890abcdef  
Content-Type: application/json

{
  "fundingRequestIds": ["fund-003", "fund-004"]
}
```

#### Request Fields
- `name` (string, optional): Updated user name
- `email` (string, optional): Updated email address (must be unique)
- `walletBalance` (number, optional): Set absolute wallet balance
- `walletAdjustment` (number, optional): Add/subtract from current balance (positive/negative values)
- `fundingRequestIds` (array, optional): Funding request IDs to append (avoids duplicates)

#### Success Response (200 OK)
```json
{
  "success": true,
  "message": "User updated successfully",
  "data": {
    "id": "674c8b3d1234567890abcdef",
    "name": "John Smith",
    "email": "john.smith@example.com", 
    "roles": ["SUPPLIER", "FUNDER"],
    "verified": false,
    "walletBalance": 1300.00,
    "fundingRequestIds": ["fund-001", "fund-002", "fund-003", "fund-004"],
    "createdAt": "2025-11-19T20:15:30.123",
    "updatedAt": "2025-11-19T20:35:20.456"
  },
  "timestamp": "2025-11-19T20:35:20.789"
}
```

#### Error Responses
```json
// 400 Bad Request - Insufficient Funds
{
  "success": false,
  "error": "Insufficient funds. Current balance: 1000, Requested deduction: 2000",
  "timestamp": "2025-11-19T20:35:20.789"  
}

// 400 Bad Request - Validation Error
{
  "success": false,
  "error": "At least one field (name, email, walletBalance, walletAdjustment, or fundingRequestIds) must be provided for update",
  "timestamp": "2025-11-19T20:35:20.789"
}

// 404 Not Found
{
  "success": false,
  "error": "User not found", 
  "timestamp": "2025-11-19T20:35:20.789"
}
```


---

### Delete User

**DELETE** `/api/v1/users/{id}`

Delete a user account from the system.

#### Request
```http
DELETE /api/v1/users/674c8b3d1234567890abcdef
```

#### URL Parameters
- `id` (string, required): MongoDB ObjectId (24 hex characters)

#### Success Response (200 OK)
```json
{
  "success": true,
  "message": "User deleted successfully", 
  "timestamp": "2025-11-19T20:40:15.123"
}
```

#### Error Responses
```json
// 400 Bad Request - Invalid ID
{
  "success": false,
  "error": "Invalid user ID format",
  "timestamp": "2025-11-19T20:40:15.123"
}

// 404 Not Found
{
  "success": false,
  "error": "User not found",
  "timestamp": "2025-11-19T20:40:15.123"
}

// 500 Internal Server Error
{
  "success": false,
  "error": "Failed to delete user", 
  "timestamp": "2025-11-19T20:40:15.123"
}
```


---

### Validate User

**POST** `/api/v1/auth/user/validate`

Validate user credentials for service-to-service authentication.

#### Request
```http
POST /api/v1/auth/user/validate
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "securepassword123"
}
```

#### Request Fields
- `email` (string, required): User's email address
- `password` (string, required): User's password

#### Success Response (200 OK)
```json
{
  "success": true,
  "message": "User validation successful",
  "data": {
    "id": "674c8b3d1234567890abcdef",
    "name": "John Doe", 
    "email": "john.doe@example.com",
    "roles": ["SUPPLIER", "FUNDER"],
    "verified": false,
    "walletBalance": 1000.00,
    "fundingRequestIds": ["fund-001", "fund-002"],
    "createdAt": "2025-11-19T20:15:30.123",
    "updatedAt": "2025-11-19T20:35:20.456"
  },
  "timestamp": "2025-11-19T20:45:12.678"
}
```

#### Error Responses
```json
// 401 Unauthorized - Invalid Credentials
{
  "success": false,
  "error": "Invalid credentials",
  "timestamp": "2025-11-19T20:45:12.678"
}

// 400 Bad Request - Missing Fields
{
  "success": false, 
  "error": "Email is required",
  "timestamp": "2025-11-19T20:45:12.678"
}
```


---

### Health Check

**GET** `/api/v1/health`

Check service health status for monitoring and deployment verification.

#### Request  
```http
GET /api/v1/health
```

#### Success Response (200 OK)
```json
{
  "success": true,
  "message": "Service is healthy",
  "data": {
    "status": "UP",
    "service": "user-service", 
    "timestamp": 1700420567890
  },
  "timestamp": "2025-11-19T20:50:05.123"
}
```

---

## Testing with Bruno

Import the Bruno collection from `bruno-user-service/` directory and run tests against your local environment.
