package com.example.auth.web;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String token, String message) {
}
