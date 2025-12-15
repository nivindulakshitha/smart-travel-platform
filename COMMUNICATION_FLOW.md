# Smart Travel Platform — Simplified

## Overview
A small Spring Boot 3 microservices demo (Java 17) that orchestrates user, flight, hotel, payment, and notification services to create bookings. Communication is mainly REST (WebClient + Feign). Data is hardcoded in-memory.

## Services (ports)
- User Service — 8081  
    - GET /users/{id} — returns user
- Flight Service — 8082  
    - GET /flights/{id} — returns flight
- Hotel Service — 8083  
    - GET /hotels/{id} — returns hotel
- Booking Service (orchestrator) — 8084  
    - POST /bookings — create booking  
    - GET /bookings/{id} — get booking  
    - POST /bookings/confirm — payment callback
- Payment Service — 8085  
    - POST /payments — process payment (calls booking confirm)
- Notification Service — 8086  
    - POST /notify — send notification

## Communication
- WebClient (reactive) — Booking ⇄ User, Payment, Notification; Payment → Booking callback  
- Feign (sync) — Booking → Flight, Hotel

## Typical Booking Flow
1. Client POST /bookings → Booking validates user, checks flight & hotel, creates PENDING booking.  
2. Booking requests payment. Payment processes and POSTs /bookings/confirm.  
3. On confirmation Booking marks CONFIRMED and calls Notification to inform user.

Console prefixes: [BOOKING], [PAYMENT], [NOTIFICATION]

## Error Handling
- Global @RestControllerAdvice in each service  
- Common 404s: UserNotFound, FlightNotFound, HotelNotFound  
- Booking errors → 400  
- Error payload: { "error": "...", "timestamp": "..." }

## Quick Start
1. Build each service (mvn package -DskipTests).  
2. Run services (start Booking last). Ports: 8081–8086.  
3. Create booking:
     curl -X POST http://localhost:8084/bookings -H "Content-Type: application/json" -d '{"userId":1,"flightId":1,"hotelId":1,"travelDate":"2025-12-25"}'

## Postman
Import SmartTravelPlatform.postman_collection.json. Test services then orchestrator. Use environment variable for bookingId.

## Notes / Extending
- No external DB; add persistence if needed.  
- Add new services and integrate via WebClient or Feign; update Postman and docs.

