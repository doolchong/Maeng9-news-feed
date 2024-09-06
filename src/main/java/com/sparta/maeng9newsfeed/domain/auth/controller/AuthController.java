package com.sparta.maeng9newsfeed.domain.auth.controller;

import com.sparta.maeng9newsfeed.annotation.Auth;
import com.sparta.maeng9newsfeed.common.dto.AuthUser;
import com.sparta.maeng9newsfeed.config.JwtUtil;
import com.sparta.maeng9newsfeed.domain.auth.dto.request.LoginRequest;
import com.sparta.maeng9newsfeed.domain.auth.dto.request.SignoutRequest;
import com.sparta.maeng9newsfeed.domain.auth.dto.request.SignupRequest;
import com.sparta.maeng9newsfeed.domain.auth.dto.response.LogoutResponse;
import com.sparta.maeng9newsfeed.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/auth/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequest signupRequest,
                                         BindingResult result) {
        if (result.hasErrors()) {
            // 검증 실패 시 첫 번째 오류 메시지 반환
            return ResponseEntity.badRequest().body(result.getAllErrors().get(0).getDefaultMessage());
        }
        String bearerToken = authService.signup(signupRequest);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest,
                                      HttpServletResponse response) {
        String token = authService.login(loginRequest);
        jwtUtil.addJwtToCookie(token, response);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<LogoutResponse> logout(HttpServletResponse response) {
        LogoutResponse logoutResponse = authService.logout(response);

        return ResponseEntity.ok(logoutResponse);
    }

    @DeleteMapping("/signout")
    public ResponseEntity<String> signout(@Auth AuthUser authUser,
                                          @RequestBody SignoutRequest signoutRequest) {
        return ResponseEntity.ok(authService.signout(authUser.getId(), signoutRequest));
    }
}
