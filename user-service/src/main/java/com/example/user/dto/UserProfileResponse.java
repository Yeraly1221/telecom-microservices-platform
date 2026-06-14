package com.example.user.dto;

import lombok.Builder;

@Builder
public record UserProfileResponse(Long id,
        String firstName,
        String lastName,
        Long  credentialsId
) {
}
