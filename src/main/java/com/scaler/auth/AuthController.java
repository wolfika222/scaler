package com.scaler.auth;

import com.scaler.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // In-memory user
    private final String dummyUsername = "admin";
    private final String dummyPassword = "$2a$10$dzgFTd3FO3RtaM8rDhDXeeAb.BejKrJQCSbdmKb2SN606wYjCME/C";

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if (!username.equals(dummyUsername) || !passwordEncoder.matches(password, dummyPassword)) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(username, Set.of("ROLE_READ", "ROLE_WRITE"));
        return ResponseEntity.ok(Map.of("token", token));
    }
}