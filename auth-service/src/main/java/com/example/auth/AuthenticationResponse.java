package com.example.auth;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String token, String message) {
}
