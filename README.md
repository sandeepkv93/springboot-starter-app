# README

# 🚀 Spring Boot Authentication Starter

A robust, production-ready Spring Boot starter template featuring comprehensive user authentication, role-based access control (RBAC), and JWT token management. Built with modern best practices and a modular architecture.

## ✨ Key Features

- 🔐 Advanced JWT Authentication & Authorization
- 👥 Role-Based Access Control (RBAC)
- 🏗️ Modular Architecture
- 🔒 Secure Password Handling with BCrypt
- 🌐 RESTful API Design
- 📝 Comprehensive API Documentation
- ⚡ Performance Optimized
- 🔍 Global Exception Handling
- 🎯 Request Validation
- 📊 PostgreSQL Integration

## 🏛️ Project Structure

```text
springboot-starter-app/
├── user-auth/           # Core authentication module
├── dummy-module/        # Example integration module
└── web-app/            # Main application module
```

## 🛠️ Tech Stack

- ☕ Java 17
- 🍃 Spring Boot 3.4.1
- 🛡️ Spring Security
- 🎟️ JWT Authentication
- 🐘 PostgreSQL
- 📝 Project Lombok
- 🏗️ Maven

## ⚙️ Prerequisites

- Java JDK 17+
- Maven 3.6+
- PostgreSQL 12+
- Docker (optional)

## 🚀 Getting Started

1. **Clone the Repository**

```bash
git clone <https://github.com/yourusername/springboot-starter-app.git>
cd springboot-starter-app
```

1. Configure Database

```bash
# web-app/src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/springbootdb
    username: your_username
    password: your_password
```

1. Build & Run

```bash
mvn clean install
cd web-app
mvn spring-boot:run
```

## 🔑 Authentication API Endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/api/v1/auth/signup` | Register new user |
| POST | `/api/v1/auth/login` | User login |
| GET | `/api/v1/auth/profile` | Get current user |
| POST | `/api/v1/auth/logout` | Logout user |
| POST | `/api/v1/auth/change-password` | Change password |
| DELETE | `/api/v1/auth/delete` | Delete account |

## 👥 Role Management API

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/v1/roles` | Get all roles |
| POST | `/api/v1/roles` | Create new role |
| PUT | `/api/v1/roles/{id}` | Update role |
| DELETE | `/api/v1/roles/{id}` | Delete role |

## 📝 API Examples

### User Registration

```http
POST /api/v1/auth/signup
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "SecureP@ss123",
    "firstName": "John",
    "lastName": "Doe"
}
```

**Response**:

```json
{
    "id": 1,
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "message": "User registered successfully"
}
```

### User Login

```http
POST /api/v1/auth/login
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "SecureP@ss123"
}

```

**Response**:

```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5..."
}
```

### Get Current User Profile

```http
GET /api/v1/auth/profile
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...
```

**Response**:

```json
{
    "id": 1,
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "roles": ["ROLE_USER"],
    "permissions": ["user:read"]
}
```

### Create New Role (Admin Only)

```http
POST /api/v1/roles
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...
Content-Type: application/json

{
    "name": "ROLE_MANAGER",
    "permissions": [
        "USER_READ",
        "USER_UPDATE",
        "ROLE_READ"
    ]
}
```

**Response**:

```json
{
    "id": 3,
    "name": "ROLE_MANAGER",
    "permissions": [
        "USER_READ",
        "USER_UPDATE",
        "ROLE_READ"
    ]
}
```

### Update User Roles

```http
PUT /api/v1/users/1/roles
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...
Content-Type: application/json

{
    "roleNames": ["ROLE_USER", "ROLE_MANAGER"]
}
```

**Response**:

```json
{
    "id": 1,
    "email": "user@example.com",
    "roles": ["ROLE_USER", "ROLE_MANAGER"],
    "permissions": ["user:read", "user:update"]
}
```

### Change Password

```http
POST /api/v1/auth/change-password?oldPassword=SecureP@ss123&newPassword=NewP@ss123
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...
```

**Response**:

```json
{
    "message": "Password changed successfully"
}
```

### Error Response Examples

**400 Bad Request**:

```json
{
    "message": "Validation failed",
    "errors": {
        "email": "Email is already in use",
        "password": "Password must contain at least one uppercase letter"
    }
}
```

**401 Unauthorized**:

```json
{
    "message": "Invalid credentials"
}
```

**403 Forbidden**:

```json
{
    "message": "Access denied: Insufficient permissions"
}
```

**404 Not Found**:

```json
{
    "message": "User not found"
}
```

## 🔒 Security Features

- 🔑 JWT Token Authentication
- ⏰ Configurable Token Expiration
- 🔐 BCrypt Password Encoding
- 🛡️ Role-Based Access Control
- 🚫 CSRF Protection
- 📡 Stateless Session Management

## 🏗️ Module Overview

### 🔐 user-auth

- Core authentication & authorization
- JWT token management
- User & role management
- Security configurations

### 🎯 dummy-module

- Integration example
- Protected endpoints
- Service implementation patterns

### 🌐 web-app

- Main application configuration
- Database integration
- Module orchestration
- Application bootstrapping

## ⚙️ Configuration

```yaml
# Key application properties
jwt:
  secret: your_jwt_secret_key
  expiration: 86400000# 24 hours
  refresh-expiration: 604800000# 7 days

spring:
  jpa:
    hibernate:
      ddl-auto: update
    database: postgresql
```