package com.example.user.web.dto;

public record UserProfileResponse(
        Long id,
        String firstName,
        String lastName
) {
}
