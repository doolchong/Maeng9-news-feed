package com.sparta.maeng9newsfeed.service;

import com.sparta.maeng9newsfeed.config.JwtUtil;
import com.sparta.maeng9newsfeed.config.PasswordEncoder;
import com.sparta.maeng9newsfeed.dto.LoginRequest;
import com.sparta.maeng9newsfeed.dto.LogoutResponse;
import com.sparta.maeng9newsfeed.dto.SignupRequest;
import com.sparta.maeng9newsfeed.entity.User;
import com.sparta.maeng9newsfeed.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String signup(SignupRequest signupRequest) {
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        User newUser = new User(
                signupRequest.getUserName(),
                signupRequest.getEmail(),
                encodedPassword,
                "한 줄 소개를 입력해주세요",
                true
        );
        User saveduser = userRepository.save(newUser);

        return jwtUtil.createToken(saveduser.getId(), saveduser.getEmail());
    }

    @Transactional
    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        // 입력된 비밀번호와 저장된 비밀번호를 비교
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.createToken(user.getId(), user.getEmail());
    }

    public LogoutResponse logout(HttpServletResponse response) {
        jwtUtil.expireCookie(response);
        return new LogoutResponse(200, "로그아웃 성공.");
    }
}
