# Smart Travel Platform - Microservices Architecture

## Overview

The Smart Travel Platform is a distributed microservices system built with Spring Boot 3 that demonstrates modern inter-service communication patterns. The platform enables users to create travel bookings by orchestrating multiple services (users, flights, hotels, payments, and notifications).

**Architecture Type:** Event-driven microservices with REST APIs  
**Framework:** Spring Boot 3.5.8  
**Language:** Java 17  
**Communication:** Synchronous REST (WebClient + Feign)  
**Data Storage:** In-memory HashMaps (hardcoded data)  
**HTTP Clients:** Spring WebFlux WebClient + Spring Cloud OpenFeign

---

## Microservices Details

### 1. User Service (Port 8081)

**Purpose:** User information provider and validator

**Endpoints:**

-   `GET /users/{id}` - Retrieve user by ID

**Response:**

```json
{
	"userId": 1,
	"name": "John Doe",
	"email": "john@travel.com"
}
```

**Hardcoded Users:**

-   User 1: John Doe (john@travel.com)
-   User 2: Jane Smith (jane@travel.com)
-   User 3: Bob Johnson (bob@travel.com)

**Called By:** Booking Service (validation step)  
**Calls:** None (data provider)

---

### 2. Flight Service (Port 8082)

**Purpose:** Flight availability and pricing information

**Endpoints:**

-   `GET /flights/{id}` - Retrieve flight by ID

**Response:**

```json
{
	"flightId": 1,
	"origin": "New York",
	"destination": "Los Angeles",
	"available": true,
	"price": 299.99
}
```

**Hardcoded Flights:**

-   Flight 1: NYC → LA ($299.99)
-   Flight 2: Chicago → Miami ($349.99)
-   Flight 3: Boston → Denver ($329.99)
-   Flight 4: Seattle → San Francisco ($289.99)
-   Flight 5: Dallas → Houston ($199.99)

**Called By:** Booking Service (availability check)  
**Integration:** Feign Client  
**Calls:** None (data provider)

---

### 3. Hotel Service (Port 8083)

**Purpose:** Hotel availability and nightly rate information

**Endpoints:**

-   `GET /hotels/{id}` - Retrieve hotel by ID

**Response:**

```json
{
	"hotelId": 1,
	"name": "Grand Plaza Hotel",
	"available": true,
	"pricePerNight": 150.0
}
```

**Hardcoded Hotels:**

-   Hotel 1: Grand Plaza Hotel ($150.00/night)
-   Hotel 2: Beachfront Resort ($200.00/night)
-   Hotel 3: Mountain Lodge ($175.00/night)
-   Hotel 4: Downtown Plaza ($125.00/night)
-   Hotel 5: Luxury Penthouse ($350.00/night)

**Called By:** Booking Service (availability check)  
**Integration:** Feign Client  
**Calls:** None (data provider)

---

### 4. Payment Service (Port 8085)

**Purpose:** Process payments and trigger booking confirmation

**Endpoints:**

-   `POST /payments` - Process payment

**Request:**

```json
{
	"bookingId": 1,
	"amount": 449.99
}
```

**Response:**

```json
{
	"success": true,
	"message": "Payment processed successfully",
	"transactionId": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Called By:** Booking Service (payment processing)  
**Integration:** WebClient (reactive HTTP)  
**Calls:** Booking Service POST `/bookings/confirm` to confirm the booking

**Key Features:**

-   Generates UUID transaction IDs
-   Calls back to Booking Service to confirm payment success
-   Simulates payment processing with unique transaction identifiers

---

### 5. Notification Service (Port 8086)

**Purpose:** Send notifications to users about their bookings

**Endpoints:**

-   `POST /notify` - Send notification

**Request:**

```json
{
	"bookingId": 1,
	"message": "Your booking has been confirmed for 2025-12-25"
}
```

**Response:**

```json
{
	"success": true,
	"message": "Notification sent successfully"
}
```

**Called By:** Booking Service (after payment processing)  
**Integration:** WebClient (reactive HTTP)  
**Calls:** None (final notification step)

**Key Features:**

-   Logs [NOTIFICATION] messages to console
-   Simple notification endpoint for booking confirmations

---

### 6. Booking Service (Port 8084) - MAIN ORCHESTRATOR

**Purpose:** Coordinate all microservices to create complete travel bookings

**Endpoints:**

-   `POST /bookings` - Create new booking
-   `GET /bookings/{id}` - Get booking details
-   `POST /bookings/confirm` - Confirm booking (called by Payment Service)

**Request (Create Booking):**

```json
{
	"userId": 1,
	"flightId": 1,
	"hotelId": 1,
	"travelDate": "2025-12-25"
}
```

**Response:**

```json
{
	"bookingId": 1,
	"userId": 1,
	"flightId": 1,
	"hotelId": 1,
	"travelDate": "2025-12-25",
	"totalCost": 449.99,
	"status": "CONFIRMED"
}
```

**Called By:** Client applications  
**Integration:** WebClient + Feign Clients  
**Calls:** All other services

---

## Communication Flow - Complete Booking Workflow

When a client sends a POST request to `http://localhost:8084/bookings`, the following orchestration flow occurs:

### Console Output:

```
[BOOKING] Creating booking for User: 1
[BOOKING] User validated: 1
[BOOKING] Created booking: 1 with status: PENDING
[PAYMENT] Processing payment - BookingID: 1 | Amount: $299.99 | TransactionID: 550e8400-e29b-41d4-a716-446655440000
[PAYMENT] Booking confirmed for ID: 1
[NOTIFICATION] BookingID: 1 | Message: Booking confirmed for 2025-12-25
[BOOKING] Payment processed for booking: 1
[BOOKING] Notification sent for booking: 1
[BOOKING] Updated booking 1 to status: CONFIRMED
```

---

## Inter-Service Communication Patterns

### Pattern 1: WebClient (Reactive, Non-Blocking)

Used for asynchronous HTTP calls with backpressure support.

**Services Using WebClient:**

-   **Booking Service**: Calls User Service, Payment Service, Notification Service
-   **Payment Service**: Calls back to Booking Service for confirmation

**Example:**

```java
@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }
}

// Usage
webClient.get()
    .uri("http://localhost:8081/users/{id}", userId)
    .retrieve()
    .bodyToMono(UserResponse.class)
    .block(); // blocking for synchronous context
```

### Pattern 2: Feign (Declarative HTTP Client)

Used for simple, synchronous REST calls with minimal configuration.

**Services Using Feign:**

-   **Booking Service**: Calls Flight Service and Hotel Service

**Example:**

```java
@FeignClient(name = "flight-service", url = "http://localhost:8082")
public interface FlightClient {
    @GetMapping("/flights/{id}")
    FlightResponse getFlightById(@PathVariable Long id);
}
```

---

## Error Handling & Exceptions

Each service implements global exception handling with `@RestControllerAdvice`.

### Exception Types:

1. **UserNotFoundException** (User Service)

    - Status: 404
    - When: User ID not found

2. **FlightNotFoundException** (Flight Service)

    - Status: 404
    - When: Flight ID not found

3. **HotelNotFoundException** (Hotel Service)

    - Status: 404
    - When: Hotel ID not found

4. **BookingException** (Booking Service)
    - Status: 400
    - When: User validation fails or other business logic errors

### Error Response Format:

```json
{
	"error": "User not found with id: 999",
	"timestamp": "2025-12-15T10:30:45.123456789"
}
```

---

## How to Use the Postman Collection

### 1. Import the Collection

1. Open Postman
2. Click **Import** button
3. Select the file: `SmartTravelPlatform.postman_collection.json`
4. Collection imported successfully!

### 2. Organize in Folders

The collection is pre-organized into folders:

-   **User Service (Port 8081)** - Tests for user endpoints
-   **Flight Service (Port 8082)** - Tests for flight endpoints
-   **Hotel Service (Port 8083)** - Tests for hotel endpoints
-   **Payment Service (Port 8085)** - Tests for payment processing
-   **Notification Service (Port 8086)** - Tests for notifications
-   **Booking Service (Port 8084)** - Main orchestrator tests
-   **Error Scenarios** - Tests for error handling

### 3. Run Tests Sequentially

**Recommended Order:**

1. Start all services in separate terminals
2. Test individual services first:
    - Get User 1
    - Get Flight 1
    - Get Hotel 1
3. Then test orchestrator:
    - Create Booking (main test)
    - Get Booking by ID
4. Test error scenarios:
    - Get Non-existent User
    - Create Booking with Invalid User

### 4. Use Environment Variables

To reuse booking IDs across requests:

1. Create a Postman environment
2. In "Create Booking" test script, `bookingId` is automatically saved
3. Reference in subsequent requests: `{{bookingId}}`

### 5. View Test Results

Each request has built-in tests:

-   Status code validation
-   Response schema validation
-   Required field presence checks

Look at the **Tests** tab after sending a request to see results.

## Quick Start Guide

### 1. Build All Services

```bash
# In smart-travel-platform directory
for service in user-service flight-service hotel-service payment-service notification-service booking-service; do
  cd $service
  bash mvnw clean package -DskipTests
  cd ..
done
```

### 2. Run All Services (in separate terminals)

```bash
# Terminal 1
cd user-service && java -jar target/travel-0.0.1-SNAPSHOT.jar

# Terminal 2
cd flight-service && java -jar target/travel-0.0.1-SNAPSHOT.jar

# Terminal 3
cd hotel-service && java -jar target/travel-0.0.1-SNAPSHOT.jar

# Terminal 4
cd payment-service && java -jar target/travel-0.0.1-SNAPSHOT.jar

# Terminal 5
cd notification-service && java -jar target/travel-0.0.1-SNAPSHOT.jar

# Terminal 6 (start last - depends on others)
cd booking-service && java -jar target/travel-0.0.1-SNAPSHOT.jar
```

### 3. Test Complete Booking Workflow

```bash
curl -X POST http://localhost:8084/bookings \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "flightId": 1, "hotelId": 1, "travelDate": "2025-12-25"}' | jq
```

---

## Key Architectural Features

✅ **Microservices Pattern**: Each service has a single responsibility  
✅ **Service Orchestration**: Booking Service coordinates complex workflows  
✅ **Reactive Communication**: WebClient for non-blocking async calls  
✅ **Declarative Clients**: Feign for simple, contract-based integration  
✅ **Error Handling**: Global exception handlers with consistent error responses  
✅ **In-Memory Storage**: No external database dependencies  
✅ **Clean Code**: Constructor injection, Lombok annotations  
✅ **Easy Testing**: Postman collection with pre-built requests  
✅ **Hardcoded Data**: No setup required - services work immediately

---

## Monitoring & Debugging

### Console Logs

Each service logs operations with prefixes:

-   `[BOOKING]` - Booking Service logs
-   `[PAYMENT]` - Payment Service logs
-   `[NOTIFICATION]` - Notification Service logs

### Postman Test Results

-   Green checkmark (✓) = Test passed
-   Red X (✗) = Test failed
-   View test output in **Test Results** tab

### Error Responses

All errors return JSON with:

-   `error`: Description of the error
-   `timestamp`: When the error occurred

---

## Extending the Platform

To add new services:

1. **Create service directory** with standard structure
2. **Implement Controller** with REST endpoints
3. **Implement Service** with business logic
4. **Create DTOs** for request/response serialization
5. **Add Exception Handling** with GlobalExceptionHandler
6. **Integrate with Booking Service** via WebClient or Feign
7. **Update Postman Collection** with new endpoints
8. **Update this README** with service details

---

## Troubleshooting

### Service won't start

-   Check if port is already in use
-   Verify Java 17 is installed: `java -version`
-   Check logs for configuration errors

### Connection refused errors

-   Ensure all 6 services are running
-   Start Booking Service last (it depends on others)
-   Verify ports: 8081, 8082, 8083, 8084, 8085, 8086

### Postman requests failing

-   Verify services are running: `curl http://localhost:8081/users/1`
-   Check request format matches expected schema
-   View service console for error logs

---

## Summary

The Smart Travel Platform demonstrates a complete microservices ecosystem with:

-   **6 independent services** communicating via REST
-   **Multiple HTTP client patterns** (WebClient + Feign)
-   **Complete orchestration workflow** with error handling
-   **Pre-built Postman collection** for easy testing
-   **Production-ready code** with clean architecture

**Start building with:** `SmartTravelPlatform.postman_collection.json`

---
