package com.travel.travel.service;

import com.travel.travel.dto.HotelResponse;
import com.travel.travel.exception.HotelNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class HotelService {

    private static final Map<Long, HotelResponse> hotels = new HashMap<>();

    static {
        hotels.put(1L, new HotelResponse(1L, "Grand Plaza Hotel", true, new BigDecimal("150.00")));
        hotels.put(2L, new HotelResponse(2L, "Sunset Beach Resort", true, new BigDecimal("200.00")));
        hotels.put(3L, new HotelResponse(3L, "Downtown Inn", false, new BigDecimal("89.99")));
        hotels.put(4L, new HotelResponse(4L, "Mountain View Lodge", true, new BigDecimal("120.00")));
        hotels.put(5L, new HotelResponse(5L, "Luxury Penthouse Hotel", true, new BigDecimal("350.00")));
    }

    public HotelResponse getHotelById(Long id) {
        return hotels.values().stream()
                .filter(hotel -> hotel.getHotelId().equals(id))
                .findFirst()
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found with id: " + id));
    }
}
