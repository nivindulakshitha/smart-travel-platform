package com.travel.travel.service;

import com.travel.travel.dto.UserResponse;
import com.travel.travel.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private static final Map<Long, UserResponse> users = new HashMap<>();

    static {
        users.put(1L, new UserResponse(1L, "John Doe", "john@travel.com"));
        users.put(2L, new UserResponse(2L, "Jane Smith", "jane@travel.com"));
        users.put(3L, new UserResponse(3L, "Bob Johnson", "bob@travel.com"));
    }

    public UserResponse getUserById(Long id) {
        return users.values().stream()
                .filter(user -> user.getUserId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }
}
