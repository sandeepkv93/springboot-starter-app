# Spring Boot Starter Application

A modular Spring Boot application demonstrating user authentication, JWT token management, and module integration. This project provides a robust starting point for building secure Spring Boot applications with user management capabilities.

## Project Structure

The project is organized into three main modules:

- **user-auth**: Core authentication and user management module
- **dummy-module**: Example module demonstrating integration with user-auth
- **web-app**: Main application module tying everything together

### Technology Stack

- Java 17
- Spring Boot 3.4.1
- Spring Security
- JWT Authentication
- PostgreSQL
- Project Lombok
- Maven

## Features

- User authentication and authorization
- JWT token-based security
- Modular architecture
- RESTful API endpoints
- Database persistence with JPA/Hibernate
- Password encryption with BCrypt
- Global exception handling
- Database schema auto-generation

## Prerequisites

- JDK 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- Docker (optional, for containerization)

## Getting Started

### 1. Clone the Repository

```bash
git clone git@github.com:sandeepkv93/springboot-starter-app.git
cd springboot-starter-app
```

### 2. Configure Database

Create a PostgreSQL database and update the configuration in `web-app/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/springbootdb
    username: ***REMOVED***
    password: ***REMOVED***
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
cd web-app
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication Endpoints

All authentication endpoints are prefixed with `/api/v1/auth`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/signup` | Register a new user |
| POST | `/login` | Authenticate user and get tokens |
| POST | `/logout` | Logout user |
| POST | `/change-password` | Change user password |
| DELETE | `/delete` | Delete user account |

### Test Endpoints

All test endpoints are prefixed with `/api/v1/test`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/public` | Public endpoint (no auth required) |
| GET | `/protected` | Protected endpoint (requires authentication) |

## Request Examples

### User Registration

```json
POST /api/v1/auth/signup
{
    "email": "user@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
}
```

### User Login

```json
POST /api/v1/auth/login
{
    "email": "user@example.com",
    "password": "password123"
}
```

## Security Configuration

The application uses JWT (JSON Web Tokens) for authentication. Key security settings:

- Token expiration: 24 hours (configurable in application.yml)
- Password encoding: BCrypt
- CSRF protection: Disabled for API endpoints
- Session management: Stateless

## Module Details

### user-auth Module

Core authentication module providing:
- User management
- JWT token handling
- Security configuration
- Authentication filters

### dummy-module

Example module demonstrating:
- Protected endpoints
- Integration with user-auth module
- Custom service implementation

### web-app Module

Main application module:
- Application configuration
- Database settings
- Module integration
- Main application entry point

## Configuration Properties

Key configuration properties in `application.yml`:

```yaml
jwt:
  secret: [your-secret-key]
  expiration: 86400000 # 24 hours in milliseconds
  refresh-expiration: 604800000 # 7 days in milliseconds

spring:
  jpa:
    hibernate:
      ddl-auto: update
    database: postgresql
```
