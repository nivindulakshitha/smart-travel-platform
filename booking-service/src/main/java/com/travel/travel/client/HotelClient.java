package com.travel.travel.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "hotel-service", url = "http://localhost:8083")
public interface HotelClient {

    @GetMapping("/hotels/{id}")
    Map<String, Object> getHotelById(@PathVariable Long id);
}
