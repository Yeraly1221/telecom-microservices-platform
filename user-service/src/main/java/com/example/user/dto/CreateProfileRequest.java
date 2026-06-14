package com.example.user.dto;

public record CreateProfileRequest(
        String firstName,
        String lastName,
        Long credentials
) {
}
