# Auth API Guide

# Authentication API Details

## User Registration

**Endpoint:**

`POST /api/v1/auth/signup`

**Headers:**

`Content-Type: application/json`

**Request Body:**

```json
{
    "email": "user@example.com",
    "password": "P@ssw0rd123",
    "firstName": "John",
    "lastName": "Doe"
}
```

**Validation Rules:**

- **Email:** Must be valid format and unique.
- **Password:** 8-32 characters, must include at least one digit, one lowercase, one uppercase, and one special character.
- **Names:** 2-50 characters each.

**Error Cases:**

```json
{
    "message": "Validation failed",
    "errors": {
        "email": "Email already exists",
        "password": "Password must contain at least one digit"
    }
}

```

---

## Login

**Endpoint:**

`POST /api/v1/auth/login`

**Headers:**

`Content-Type: application/json`

**Request Body:**

```json
{
    "email": "user@example.com",
    "password": "P@ssw0rd123"
}

```

**Response:**

```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5..."
}

```

---

## Get Current User Profile

**Endpoint:**

`GET /api/v1/auth/profile`

**Headers:**

`Authorization: Bearer {token}`

**Response:**

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

---

# Role Management API Details

## Create New Role

**Endpoint:**

`POST /api/v1/roles`

**Headers:**

`Authorization: Bearer {token}`

`Content-Type: application/json`

**Request Body:**

```json
{
    "name": "ROLE_MANAGER",
    "permissions": [
        "USER_READ",
        "USER_UPDATE",
        "ROLE_READ"
    ]
}
```

**Validation Rules:**

- **Name:** Must start with `ROLE_`.
- **Format:** Only uppercase letters and underscores are allowed.
- **Permissions:** Must be valid and non-empty.

---

## Update Role

**Endpoint:**

`PUT /api/v1/roles/{id}`

**Headers:**

`Authorization: Bearer {token}`

`Content-Type: application/json`

**Request Body:**

```json
{
    "name": "ROLE_MANAGER",
    "permissions": [
        "USER_READ",
        "USER_UPDATE",
        "ROLE_READ",
        "USER_CREATE"
    ]
}

```

---

# User Management API Details

## Get All Users (Admin Only)

**Endpoint:**

`GET /api/v1/users`

**Headers:**

`Authorization: Bearer {token}`

**Response:**

```json
[
    {
        "id": 1,
        "email": "user@example.com",
        "firstName": "John",
        "lastName": "Doe",
        "roles": ["ROLE_USER"],
        "permissions": ["user:read"]
    },
    {
        "id": 2,
        "email": "admin@example.com",
        "firstName": "Admin",
        "lastName": "User",
        "roles": ["ROLE_ADMIN"],
        "permissions": ["user:read", "user:create", ...]
    }
]

```

---

## Update User Roles (Admin Only)

**Endpoint:**

`PUT /api/v1/users/{id}/roles`

**Headers:**

`Authorization: Bearer {token}`

`Content-Type: application/json`

**Request Body:**

```json
{
    "roleNames": ["ROLE_USER", "ROLE_MANAGER"]
}
```

---

# Common HTTP Status Codes

- **200 OK**: Successful operation
- **201 Created**: Resource created
- **400 Bad Request**: Validation error
- **401 Unauthorized**: Missing or invalid token
- **403 Forbidden**: Insufficient permissions
- **404 Not Found**: Resource not found
- **500 Server Error**: Internal error

---

# Role Hierarchy Example

```json
{
    "ROLE_USER": {
        "permissions": ["USER_READ"]
    },
    "ROLE_MANAGER": {
        "inheritsFrom": "ROLE_USER",
        "permissions": [
            "USER_READ",
            "USER_UPDATE",
            "ROLE_READ"
        ]
    },
    "ROLE_ADMIN": {
        "inheritsFrom": ["ROLE_MANAGER"],
        "permissions": [
            "USER_READ", "USER_CREATE", "USER_UPDATE", "USER_DELETE",
            "ROLE_READ", "ROLE_CREATE", "ROLE_UPDATE", "ROLE_DELETE",
            "ADMIN_ACCESS"
        ]
    }
}
```

# Example APIs

# Authentication APIs

## Sign Up

```bash
curl -X POST '<http://localhost:8080/api/v1/auth/signup>' \\
-H 'Content-Type: application/json' \\
-d '{
    "email": "user@example.com",
    "password": "P@ssw0rd123",
    "firstName": "John",
    "lastName": "Doe"
}'
```

## Login

```bash
curl -X POST 'http://localhost:8080/api/v1/auth/login' \
-H 'Content-Type: application/json' \
-d '{
    "email": "user@example.com",
    "password": "P@ssw0rd123"
}'
```

---

## Get Current User Profile

```bash
curl -X GET 'http://localhost:8080/api/v1/auth/profile' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...'
```

---

## Create Admin User (Requires ADMIN_ACCESS)

```bash
curl -X POST 'http://localhost:8080/api/v1/auth/admin/create' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...' \
-H 'Content-Type: application/json' \
-d '{
    "email": "admin@example.com",
    "password": "Admin@123",
    "firstName": "Admin",
    "lastName": "User"
}'
```

---

## Change Password

```bash
curl -X POST 'http://localhost:8080/api/v1/auth/change-password?oldPassword=P@ssw0rd123&newPassword=NewP@ssw0rd123' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...'
```

---

## Logout

```bash
curl -X POST 'http://localhost:8080/api/v1/auth/logout' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...'
```

---

## Delete Own Account

```bash
curl -X DELETE 'http://localhost:8080/api/v1/auth/delete' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...'
```

---

# Role Management APIs

## Get All Roles

```bash
curl -X GET 'http://localhost:8080/api/v1/roles' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...'
```

---

## Get Role by ID

```bash
curl -X GET 'http://localhost:8080/api/v1/roles/1' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...'
```

---

## Create New Role

```bash
curl -X POST 'http://localhost:8080/api/v1/roles' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...' \
-H 'Content-Type: application/json' \
-d '{
    "name": "ROLE_MANAGER",
    "permissions": ["USER_READ", "USER_UPDATE", "ROLE_READ"]
}'
```

---

## Update Role

```bash
curl -X PUT 'http://localhost:8080/api/v1/roles/1' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...' \
-H 'Content-Type: application/json' \
-d '{
    "name": "ROLE_MANAGER",
    "permissions": ["USER_READ", "USER_UPDATE", "ROLE_READ", "USER_CREATE"]
}'
```

---

## Delete Role

```bash
curl -X DELETE 'http://localhost:8080/api/v1/roles/1' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...'
```

---

# User Management APIs

## Get All Users

```bash
curl -X GET 'http://localhost:8080/api/v1/users' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...'
```

---

## Get User by ID

```bash
curl -X GET 'http://localhost:8080/api/v1/users/1' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...'
```

---

## Update User Roles

```bash
curl -X PUT 'http://localhost:8080/api/v1/users/1/roles' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...' \
-H 'Content-Type: application/json' \
-d '{
    "roleNames": ["ROLE_USER", "ROLE_MANAGER"]
}'
```

---

## Delete User

```bash
curl -X DELETE 'http://localhost:8080/api/v1/users/1' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...'
```

# Example Response

## **Authentication APIs**

### **Sign Up**

**Request:**

```bash
curl -X POST 'http://localhost:8080/api/v1/auth/signup' \
-H 'Content-Type: application/json' \
-d '{
    "email": "user@example.com",
    "password": "P@ssw0rd123",
    "firstName": "John",
    "lastName": "Doe"
}'
```

**Response:**

```json

{
    "id": 1,
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "message": "User registered successfully"
}
```

---

### **Login**

**Request:**

```bash
curl -X POST 'http://localhost:8080/api/v1/auth/login' \
-H 'Content-Type: application/json' \
-d '{
    "email": "user@example.com",
    "password": "P@ssw0rd123"
}'
```

**Response:**

```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

### **Get Current User Profile**

**Request:**

```bash
curl -X GET 'http://localhost:8080/api/v1/auth/profile' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...'
```

**Response:**

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

---

### **Create Admin User**

**Request:**

```bash
curl -X POST 'http://localhost:8080/api/v1/auth/admin/create' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...' \
-H 'Content-Type: application/json' \
-d '{
    "email": "admin@example.com",
    "password": "Admin@123",
    "firstName": "Admin",
    "lastName": "User"
}'
```

**Response:**

```json
{
    "id": 2,
    "email": "admin@example.com",
    "firstName": "Admin",
    "lastName": "User",
    "message": "Admin user created successfully"
}
```

---

### **Change Password**

**Request:**

```bash
curl -X POST 'http://localhost:8080/api/v1/auth/change-password?oldPassword=P@ssw0rd123&newPassword=NewP@ssw0rd123' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...'
```

**Response:**

```json
{
    "message": "Password changed successfully"
}
```

---

## **Role Management APIs**

### **Get All Roles**

**Request:**

```bash
curl -X GET 'http://localhost:8080/api/v1/roles' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...'
```

**Response:**

```json
[
    {
        "id": 1,
        "name": "ROLE_USER",
        "permissions": ["USER_READ"]
    },
    {
        "id": 2,
        "name": "ROLE_ADMIN",
        "permissions": [
            "USER_READ", "USER_CREATE", "USER_UPDATE",
            "USER_DELETE", "ROLE_READ", "ROLE_CREATE",
            "ROLE_UPDATE", "ROLE_DELETE", "ADMIN_ACCESS"
        ]
    }
]
```

---

### **Create New Role**

**Request:**

```bash
curl -X POST 'http://localhost:8080/api/v1/roles' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...' \
-H 'Content-Type: application/json' \
-d '{
    "name": "ROLE_SUPERVISOR",
    "permissions": ["USER_READ", "USER_UPDATE"]
}'
```

**Response:**

```json
{
    "id": 4,
    "name": "ROLE_SUPERVISOR",
    "permissions": ["USER_READ", "USER_UPDATE"]
}
```

---

### **Update Role**

**Request:**

```bash
curl -X PUT 'http://localhost:8080/api/v1/roles/4' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...' \
-H 'Content-Type: application/json' \
-d '{
    "name": "ROLE_SUPERVISOR",
    "permissions": ["USER_READ", "USER_UPDATE", "ROLE_READ"]
}'
```

**Response:**

```json
{
    "id": 4,
    "name": "ROLE_SUPERVISOR",
    "permissions": ["USER_READ", "USER_UPDATE", "ROLE_READ"]
}
```

---

## **User Management APIs**

### **Get All Users**

**Request:**

```bash
curl -X GET 'http://localhost:8080/api/v1/users' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...'
```

**Response:**

```json
[
    {
        "id": 1,
        "email": "user@example.com",
        "firstName": "John",
        "lastName": "Doe",
        "roles": ["ROLE_USER"],
        "permissions": ["user:read"]
    }
]
```

---

### **Update User Roles**

**Request:**

```bash
curl -X PUT 'http://localhost:8080/api/v1/users/1/roles' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...' \
-H 'Content-Type: application/json' \
-d '{
    "roleNames": ["ROLE_USER", "ROLE_SUPERVISOR"]
}'
```

**Response:**

```json
{
    "id": 1,
    "email": "user@example.com",
    "roles": ["ROLE_USER", "ROLE_SUPERVISOR"],
    "permissions": ["user:read", "user:update"]
}

```

---

## **Error Response Examples**

### **400 Bad Request**

```json
{
    "message": "Validation failed",
    "errors": {
        "email": "Email is already in use",
        "password": "Password must contain at least one uppercase letter"
    }
}
```

### **401 Unauthorized**

```json
{
    "message": "Invalid credentials"
}
```

### **403 Forbidden**

```json
{
    "message": "Access denied: Insufficient permissions"
}
```

### **404 Not Found**

```json
{
    "message": "User not found"
}
```
