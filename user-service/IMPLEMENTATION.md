"""
User Service - Complete Implementation Guide
Spring Boot 3 + Java 17 Microservice

This document provides a complete overview of the User Service implementation.
"""

## ✅ COMPLETED COMPONENTS

### 1. UserResponse DTO

-   Location: src/main/java/com/travel/travel/dto/UserResponse.java
-   Fields: userId (Long), name (String), email (String)
-   Annotations: @Data, @AllArgsConstructor, @NoArgsConstructor
-   Purpose: Data transfer object for REST responses

### 2. UserService

-   Location: src/main/java/com/travel/travel/service/UserService.java
-   Hardcoded Users:
    -   ID 1: John Doe (john@travel.com)
    -   ID 2: Jane Smith (jane@travel.com)
    -   ID 3: Bob Johnson (bob@travel.com)
-   Method: getUserById(Long id) → UserResponse
-   Throws: UserNotFoundException if user not found
-   Implementation: In-memory HashMap (no database)

### 3. UserController

-   Location: src/main/java/com/travel/travel/controller/UserController.java
-   Base Path: /users
-   Endpoint:
    -   GET /users/{id} → ResponseEntity<UserResponse>
-   Constructor Injection: UserService dependency
-   HTTP Status: 200 OK (success), 404 Not Found (error)

### 4. UserNotFoundException

-   Location: src/main/java/com/travel/travel/exception/UserNotFoundException.java
-   Extends: RuntimeException
-   Constructor: Accepts error message
-   Used by: UserService.getUserById()

### 5. GlobalExceptionHandler

-   Location: src/main/java/com/travel/travel/exception/GlobalExceptionHandler.java
-   Annotation: @RestControllerAdvice
-   Handler: handleUserNotFound(UserNotFoundException)
-   Response Format: JSON with timestamp, status, error, message
-   HTTP Status: 404 Not Found

## 📁 DIRECTORY STRUCTURE

user-service/
├── src/
│ ├── main/
│ │ ├── java/com/travel/travel/
│ │ │ ├── TravelApplication.java [Main class]
│ │ │ ├── controller/
│ │ │ │ └── UserController.java [REST endpoints]
│ │ │ ├── service/
│ │ │ │ └── UserService.java [Business logic]
│ │ │ ├── dto/
│ │ │ │ └── UserResponse.java [Data transfer object]
│ │ │ └── exception/
│ │ │ ├── UserNotFoundException.java [Custom exception]
│ │ │ └── GlobalExceptionHandler.java [Exception handling]
│ │ └── resources/
│ │ └── application.properties [Port: 8081]
│ └── test/
│ └── java/com/travel/travel/
│ └── UserControllerTest.java
├── pom.xml [Maven config]
├── mvnw, mvnw.cmd [Maven wrappers]
├── build-and-run.sh [Build script]
├── test-api.sh [API test script]
└── README.md [Documentation]

## 🚀 BUILD & RUN INSTRUCTIONS

### Build:

```bash
cd user-service
bash mvnw clean package -DskipTests
# or
bash mvnw clean compile
```

### Run:

```bash
java -jar target/travel-0.0.1-SNAPSHOT.jar
# or
bash mvnw spring-boot:run
```

### Service is running at:

http://localhost:8081

## 🧪 TESTING

### Test Endpoints:

1. Get User (Success):
   GET http://localhost:8081/users/1
   Response: {"userId":1,"name":"John Doe","email":"john@travel.com"}

2. Get Another User:
   GET http://localhost:8081/users/2
   Response: {"userId":2,"name":"Jane Smith","email":"jane@travel.com"}

3. User Not Found (Error):
   GET http://localhost:8081/users/999
   Response:
   {
   "timestamp":"2025-12-15T21:45:00",
   "status":404,
   "error":"Not Found",
   "message":"User not found with id: 999"
   }

### Using curl:

```bash
# Success case
curl http://localhost:8081/users/1

# Error case
curl http://localhost:8081/users/999
```

### Run automated tests:

```bash
bash test-api.sh
```

## 🏗️ ARCHITECTURE

```
HTTP Request
    ↓
UserController (REST Layer)
    ↓
UserService (Business Logic)
    ↓
In-Memory Data (Hardcoded Users)
    ↓
HTTP Response / Exception

Exception Handling:
    UserNotFoundException → GlobalExceptionHandler → JSON Error Response
```

## 📝 KEY FEATURES

✅ Spring Boot 3.5.8
✅ Java 17
✅ REST API (GET /users/{id})
✅ Hardcoded user data (no database)
✅ Proper DTOs (UserResponse)
✅ Exception handling (UserNotFoundException)
✅ Global exception handler (@RestControllerAdvice)
✅ Lombok annotations (@Data, @AllArgsConstructor, @NoArgsConstructor)
✅ Constructor injection (clean dependency management)
✅ Microservice-ready REST endpoints
✅ Clean architecture (controller → service → dto → exception)
✅ Minimal package structure
✅ No persistence/database
✅ No RestTemplate used
✅ Proper HTTP status codes

## 🔧 CONFIGURATION

Port: 8081 (configured in application.properties)
Application Name: travel
Framework: Spring Boot 3
Language: Java 17
Build Tool: Maven

## 📦 DEPENDENCIES

-   spring-boot-starter-web (REST support)
-   lombok (reduce boilerplate)
-   spring-boot-starter-test (testing)

## 🎯 ASSIGNMENT FOCUS

This implementation demonstrates:

-   REST API design for microservices
-   Clean architecture principles
-   Proper exception handling
-   DTO usage
-   Dependency injection
-   Spring Boot best practices
-   Java 17 features

All code is minimal, focused, and assignment-ready.
