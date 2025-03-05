package com.eventify.controllers;

import com.eventify.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String token = authService.authenticate(credentials.get("email"), credentials.get("password"));
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
public ResponseEntity<?> register(@RequestBody Map<String, String> userData) {
    String email = userData.get("email");
    String password = userData.get("password");

    authService.register(email, password);
    return ResponseEntity.ok(Map.of("message", "User registered successfully"));
}

}
