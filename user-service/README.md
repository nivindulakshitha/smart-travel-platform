# User Service - Spring Boot 3 Microservice

## Overview

User Service is a Spring Boot 3 microservice running on port **8081** that provides REST APIs for user management.

### Key Features:

-   ✅ Java 17 compatible
-   ✅ Spring Boot 3.5.8
-   ✅ No database (in-memory hardcoded data)
-   ✅ Clean Architecture with proper package structure
-   ✅ Lombok for reduced boilerplate
-   ✅ Global exception handling
-   ✅ REST API for microservice communication

## Project Structure

```
user-service/
├── src/main/java/com/travel/travel/
│   ├── controller/
│   │   └── UserController.java       # REST endpoints
│   ├── service/
│   │   └── UserService.java          # Business logic
│   ├── dto/
│   │   └── UserResponse.java         # Data Transfer Object
│   ├── exception/
│   │   ├── UserNotFoundException.java # Custom exception
│   │   └── GlobalExceptionHandler.java # Exception handling
│   └── TravelApplication.java        # Main application class
├── src/main/resources/
│   └── application.properties        # Configuration (port 8081)
├── pom.xml                           # Maven configuration
└── build-and-run.sh                  # Build and run script
```

## Components

### 1. UserResponse DTO

**File**: `src/main/java/com/travel/travel/dto/UserResponse.java`

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long userId;
    private String name;
    private String email;
}
```

### 2. UserService

**File**: `src/main/java/com/travel/travel/service/UserService.java`

-   Manages hardcoded user data
-   Provides `getUserById(Long id)` method
-   Throws `UserNotFoundException` when user not found

**Hardcoded Users:**

-   ID 1: John Doe (john@travel.com)
-   ID 2: Jane Smith (jane@travel.com)
-   ID 3: Bob Johnson (bob@travel.com)

### 3. UserController

**File**: `src/main/java/com/travel/travel/controller/UserController.java`

**Endpoint**: `GET /users/{id}`

Response (200 OK):

```json
{
	"userId": 1,
	"name": "John Doe",
	"email": "john@travel.com"
}
```

### 4. Exception Handling

**UserNotFoundException**: Thrown when user with given ID doesn't exist

**GlobalExceptionHandler**: Provides structured error responses

Error Response (404 Not Found):

```json
{
	"timestamp": "2025-12-15T21:45:00",
	"status": 404,
	"error": "Not Found",
	"message": "User not found with id: 999"
}
```

## Configuration

**File**: `src/main/resources/application.properties`

```properties
spring.application.name=travel
server.port=8081
```

## Building & Running

### Build:

```bash
cd user-service
bash ./mvnw clean package -DskipTests
```

### Run:

```bash
java -jar target/travel-0.0.1-SNAPSHOT.jar
```

Or use the provided script:

```bash
bash ./build-and-run.sh
```

## Testing

### Test User Retrieval (Success):

```bash
curl http://localhost:8081/users/1
```

Response:

```json
{
	"userId": 1,
	"name": "John Doe",
	"email": "john@travel.com"
}
```

### Test User Not Found (Error):

```bash
curl http://localhost:8081/users/999
```

Response:

```json
{
	"timestamp": "2025-12-15T21:45:00",
	"status": 404,
	"error": "Not Found",
	"message": "User not found with id: 999"
}
```

## Dependencies

-   **Spring Boot 3.5.8**
-   **Spring Web**: REST endpoints
-   **Lombok**: Reduce boilerplate code
-   **Java 17**: Latest LTS version

## Best Practices Implemented

✅ Clean Architecture (controller → service → dto → exception)
✅ Dependency Injection via constructor
✅ Global exception handling with @RestControllerAdvice
✅ Proper HTTP status codes
✅ Structured error responses
✅ DTOs for data transfer
✅ No database coupling
✅ Microservice-ready REST APIs
✅ Minimal, focused code structure

## Next Steps

To extend this service:

1. Add more endpoints (create, update, delete)
2. Implement data persistence (add JPA/Database)
3. Add authentication/authorization
4. Implement service-to-service communication
5. Add logging and monitoring
6. Configure for deployment
