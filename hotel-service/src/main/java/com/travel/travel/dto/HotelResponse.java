package com.travel.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelResponse {
    private Long hotelId;
    private String name;
    private Boolean available;
    private BigDecimal pricePerNight;
}
