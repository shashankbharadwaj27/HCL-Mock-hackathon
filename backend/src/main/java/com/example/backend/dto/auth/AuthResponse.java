package com.example.backend.dto.auth;

import com.example.backend.entity.User;
import lombok.Getter;

@Getter
public class AuthResponse {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;

    public AuthResponse(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }
}