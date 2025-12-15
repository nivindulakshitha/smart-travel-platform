package com.travel.travel.service;

import com.travel.travel.dto.FlightResponse;
import com.travel.travel.exception.FlightNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class FlightService {

    private static final Map<Long, FlightResponse> flights = new HashMap<>();

    static {
        flights.put(1L, new FlightResponse(1L, "New York", "Los Angeles", true, new BigDecimal("299.99")));
        flights.put(2L, new FlightResponse(2L, "Chicago", "Miami", true, new BigDecimal("249.99")));
        flights.put(3L, new FlightResponse(3L, "Boston", "Denver", false, new BigDecimal("199.99")));
        flights.put(4L, new FlightResponse(4L, "Seattle", "San Francisco", true, new BigDecimal("179.99")));
        flights.put(5L, new FlightResponse(5L, "Dallas", "Houston", true, new BigDecimal("89.99")));
    }

    public FlightResponse getFlightById(Long id) {
        return flights.values().stream()
                .filter(flight -> flight.getFlightId().equals(id))
                .findFirst()
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + id));
    }
}
