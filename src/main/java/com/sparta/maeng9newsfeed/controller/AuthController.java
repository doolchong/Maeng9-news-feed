package com.sparta.maeng9newsfeed.controller;

import com.sparta.maeng9newsfeed.config.JwtUtil;
import com.sparta.maeng9newsfeed.dto.LoginRequest;
import com.sparta.maeng9newsfeed.dto.SignupRequest;
import com.sparta.maeng9newsfeed.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/auth/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequest signupRequest) {
        String bearerToken = authService.signup(signupRequest);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String token = authService.login(loginRequest);
        jwtUtil.addJwtToCookie(token, response);
        return ResponseEntity.ok().build();
    }
}
