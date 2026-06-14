package com.example.auth.web.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String token, String message) {
}
