package com.travel.travel.controller;

import com.travel.travel.dto.BookingRequest;
import com.travel.travel.dto.BookingResponse;
import com.travel.travel.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request) {
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable Long id) {
        BookingResponse response = bookingService.getBooking(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmBooking(@RequestBody Map<String, Object> confirmRequest) {
        Long bookingId = ((Number) confirmRequest.get("bookingId")).longValue();
        System.out.println("[BOOKING] Confirmation request received for booking: " + bookingId);
        return ResponseEntity.ok("Booking confirmed");
    }
}
