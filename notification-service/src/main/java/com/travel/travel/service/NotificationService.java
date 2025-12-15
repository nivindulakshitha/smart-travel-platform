package com.travel.travel.service;

import com.travel.travel.dto.NotificationRequest;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendNotification(NotificationRequest request) {
        System.out.println("[NOTIFICATION] BookingID: " + request.getBookingId() +
                " | Message: " + request.getMessage());
    }
}
