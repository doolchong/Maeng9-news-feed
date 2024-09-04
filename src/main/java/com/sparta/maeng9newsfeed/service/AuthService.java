package com.sparta.maeng9newsfeed.service;

import com.sparta.maeng9newsfeed.config.JwtUtil;
import com.sparta.maeng9newsfeed.dto.LoginRequest;
import com.sparta.maeng9newsfeed.dto.SignupRequest;
import com.sparta.maeng9newsfeed.entity.User;
import com.sparta.maeng9newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public String signup(SignupRequest signupRequest) {
        User newUser = new User(signupRequest);
        User saveduser = userRepository.save(newUser);

        return jwtUtil.createToken(saveduser.getId(), saveduser.getEmail());
    }

    @Transactional
    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        return jwtUtil.createToken(user.getId(), user.getEmail());
    }
}
