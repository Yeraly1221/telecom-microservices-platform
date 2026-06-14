package com.example.auth.web;


import com.example.auth.application.AuthenticationService;
import com.example.auth.web.dto.AuthenticationRequest;
import com.example.auth.web.dto.AuthenticationResponse;
import com.example.auth.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/confirm")
    public ResponseEntity<AuthenticationResponse> confirm(@RequestParam String token) {
        return ResponseEntity.ok(authService.confirm(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
