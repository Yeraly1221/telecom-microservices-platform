package com.example.user.dto;

import lombok.Builder;

@Builder
public record UpdateProfileRequest(String firstName, String lastName) {
}
