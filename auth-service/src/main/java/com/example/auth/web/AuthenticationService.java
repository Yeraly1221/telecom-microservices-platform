package com.example.auth.web;

import com.example.auth.domain.Credentials;
import com.example.auth.domain.CredentialsRepository;
import com.example.auth.domain.Role;
import com.example.configuration.JwtService;
import com.example.email.EmailSender;
import com.example.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailSender emailSender;

    @Value("${application.base-url}")
    private String baseUrl;

    public String register(RegisterRequest registerRequest) {

        if(registerRequest == null){
            throw new RegistrationException("Registration request is required");
        }
        if(registerRequest.email() == null || registerRequest.password() == null){
            throw new IllegalArgumentException("Email or password is required");
        }

        if(credentialsRepository.existsByEmail(registerRequest.email())){
            throw new EmailAlreadyExistsException("Email already exists");
        }

        var user = Credentials
                .builder()
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .role(Role.USER)
                .enabled(false)
                .build();

        credentialsRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        String link = baseUrl + "/api/v1/auth/confirm?token=" + jwtToken;
        emailSender.send(
                registerRequest.email(),
                buildEmail(registerRequest.email(), link)
        );

        return "Check your email to confirm registration";

    }

    public AuthenticationResponse confirm(String token) {
        final String userEmail;
        try {
            userEmail = jwtService.extractUsername(token);
        } catch (InvalidTokenException ex) {
            throw new InvalidTokenException("Token expired or invalid");
        }

        Credentials credentials = credentialsRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Username not found"));

        if (!jwtService.isTokenValid(token, credentials)) {
            throw new InvalidTokenException("Token expired or invalid");
        }

        if (Boolean.TRUE.equals(credentials.getEnabled())) {
            throw new ConfirmationException("Email already confirmed");
        }

        credentials.setEnabled(true);
        credentialsRepository.save(credentials);

        String newToken = jwtService.generateToken(credentials);
        return AuthenticationResponse.builder()
                .token(newToken)
                .message("Email confirmed successfully")
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        if (request == null) {
            throw new LoginException("Login request is required");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()

                )
        );

        var user = credentialsRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("Logged in successfully")
                .build();


    }

    private String buildEmail(String name, String link) {
        //String safeName = HtmlUtils.htmlEscape(name == null ? "" : name);
        return "<div style='font-family: Arial, sans-serif; max-width: 600px;'>"
               // + "<h2>Hi " + safeName + "!</h2>"
                + "<p>Thanks for registering. Please confirm your email:</p>"
                + "<a href='" + link + "' "
                + "style='background-color: #4CAF50; color: white; padding: 14px 20px; "
                + "text-decoration: none; border-radius: 4px;'>Confirm Email</a>"
                + "<p>Link expires in 15 minutes.</p>"
                + "</div>";
    }


}
