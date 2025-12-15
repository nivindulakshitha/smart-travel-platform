package com.travel.travel.service;

import com.travel.travel.dto.BookingConfirmRequest;
import com.travel.travel.dto.PaymentRequest;
import com.travel.travel.dto.PaymentResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
public class PaymentService {

    private final WebClient webClient;
    private static final String BOOKING_SERVICE_URL = "http://localhost:8084";

    public PaymentService(WebClient webClient) {
        this.webClient = webClient;
    }

    public PaymentResponse processPayment(PaymentRequest request) {
        // Simulate payment processing
        String transactionId = UUID.randomUUID().toString();
        System.out.println("[PAYMENT] Processing payment - BookingID: " + request.getBookingId() +
                " | Amount: $" + request.getAmount() + " | TransactionID: " + transactionId);

        // Call Booking Service to confirm booking
        confirmBooking(request.getBookingId());

        return new PaymentResponse(true, "Payment processed successfully", transactionId);
    }

    private void confirmBooking(Long bookingId) {
        try {
            BookingConfirmRequest confirmRequest = new BookingConfirmRequest(bookingId, "CONFIRMED");

            webClient.post()
                    .uri(BOOKING_SERVICE_URL + "/bookings/confirm")
                    .bodyValue(confirmRequest)
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            System.out.println("[PAYMENT] Booking confirmed for ID: " + bookingId);
        } catch (Exception e) {
            System.out.println("[PAYMENT] Warning: Could not confirm booking - " + e.getMessage());
        }
    }
}
