package com.travel.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightResponse {
    private Long flightId;
    private String origin;
    private String destination;
    private Boolean available;
    private BigDecimal price;
}
