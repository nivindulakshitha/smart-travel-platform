package com.travel.travel.service;

import com.travel.travel.client.FlightClient;
import com.travel.travel.client.HotelClient;
import com.travel.travel.dto.BookingRequest;
import com.travel.travel.dto.BookingResponse;
import com.travel.travel.exception.BookingException;
import com.travel.travel.model.Booking;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BookingService {

    private final WebClient webClient;
    private final FlightClient flightClient;
    private final HotelClient hotelClient;

    private static final Map<Long, Booking> bookings = new HashMap<>();
    private static final AtomicLong bookingIdCounter = new AtomicLong(1);

    private static final String USER_SERVICE_URL = "http://localhost:8081";
    private static final String PAYMENT_SERVICE_URL = "http://localhost:8085";
    private static final String NOTIFICATION_SERVICE_URL = "http://localhost:8086";

    public BookingService(WebClient webClient, FlightClient flightClient, HotelClient hotelClient) {
        this.webClient = webClient;
        this.flightClient = flightClient;
        this.hotelClient = hotelClient;
    }

    public BookingResponse createBooking(BookingRequest request) {
        System.out.println("[BOOKING] Creating booking for User: " + request.getUserId());

        // Step 1: Validate user via WebClient
        validateUser(request.getUserId());

        // Step 2: Check flight availability via Feign
        Map<String, Object> flight = getFlightInfo(request.getFlightId());

        // Step 3: Check hotel availability via Feign
        Map<String, Object> hotel = getHotelInfo(request.getHotelId());

        // Step 4: Calculate total cost
        BigDecimal flightPrice = new BigDecimal(flight.get("price").toString());
        BigDecimal hotelPrice = new BigDecimal(hotel.get("pricePerNight").toString());
        BigDecimal totalCost = flightPrice.add(hotelPrice);

        // Step 5: Store booking with PENDING status
        Long bookingId = bookingIdCounter.getAndIncrement();
        Booking booking = new Booking(bookingId, request.getUserId(), request.getFlightId(),
                request.getHotelId(), request.getTravelDate(), totalCost, "PENDING");
        bookings.put(bookingId, booking);
        System.out.println("[BOOKING] Created booking: " + bookingId + " with status: PENDING");

        // Step 6: Call Payment Service via WebClient
        processPayment(bookingId, totalCost);

        // Step 7: Call Notification Service via WebClient
        sendNotification(bookingId, "Booking confirmed for " + request.getTravelDate());

        // Step 8: Update booking status to CONFIRMED
        booking.setStatus("CONFIRMED");
        System.out.println("[BOOKING] Updated booking " + bookingId + " to status: CONFIRMED");

        return new BookingResponse(bookingId, request.getUserId(), request.getFlightId(),
                request.getHotelId(), request.getTravelDate(), totalCost, "CONFIRMED");
    }

    private void validateUser(Long userId) {
        try {
            webClient.get()
                    .uri(USER_SERVICE_URL + "/users/{id}", userId)
                    .retrieve()
                    .toEntity(Map.class)
                    .block();
            System.out.println("[BOOKING] User validated: " + userId);
        } catch (Exception e) {
            throw new BookingException("User validation failed for ID: " + userId);
        }
    }

    private Map<String, Object> getFlightInfo(Long flightId) {
        try {
            return flightClient.getFlightById(flightId);
        } catch (Exception e) {
            throw new BookingException("Flight not found or unavailable: " + flightId);
        }
    }

    private Map<String, Object> getHotelInfo(Long hotelId) {
        try {
            return hotelClient.getHotelById(hotelId);
        } catch (Exception e) {
            throw new BookingException("Hotel not found or unavailable: " + hotelId);
        }
    }

    private void processPayment(Long bookingId, BigDecimal amount) {
        try {
            Map<String, Object> paymentRequest = new HashMap<>();
            paymentRequest.put("bookingId", bookingId);
            paymentRequest.put("amount", amount);

            webClient.post()
                    .uri(PAYMENT_SERVICE_URL + "/payments")
                    .bodyValue(paymentRequest)
                    .retrieve()
                    .toEntity(Map.class)
                    .block();
            System.out.println("[BOOKING] Payment processed for booking: " + bookingId);
        } catch (Exception e) {
            System.out.println("[BOOKING] Warning: Payment processing failed - " + e.getMessage());
        }
    }

    private void sendNotification(Long bookingId, String message) {
        try {
            Map<String, Object> notificationRequest = new HashMap<>();
            notificationRequest.put("bookingId", bookingId);
            notificationRequest.put("message", message);

            webClient.post()
                    .uri(NOTIFICATION_SERVICE_URL + "/notify")
                    .bodyValue(notificationRequest)
                    .retrieve()
                    .toEntity(Map.class)
                    .block();
            System.out.println("[BOOKING] Notification sent for booking: " + bookingId);
        } catch (Exception e) {
            System.out.println("[BOOKING] Warning: Notification failed - " + e.getMessage());
        }
    }

    public BookingResponse getBooking(Long bookingId) {
        Booking booking = bookings.values().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElseThrow(() -> new BookingException("Booking not found: " + bookingId));

        return new BookingResponse(booking.getBookingId(), booking.getUserId(), booking.getFlightId(),
                booking.getHotelId(), booking.getTravelDate(), booking.getTotalCost(), booking.getStatus());
    }
}
