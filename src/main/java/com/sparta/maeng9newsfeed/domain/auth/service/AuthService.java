package com.sparta.maeng9newsfeed.domain.auth.service;

import com.sparta.maeng9newsfeed.config.JwtUtil;
import com.sparta.maeng9newsfeed.config.PasswordEncoder;
import com.sparta.maeng9newsfeed.domain.auth.dto.request.LoginRequest;
import com.sparta.maeng9newsfeed.domain.auth.dto.response.LogoutResponse;
import com.sparta.maeng9newsfeed.domain.auth.dto.request.SignoutRequest;
import com.sparta.maeng9newsfeed.domain.auth.dto.request.SignupRequest;
import com.sparta.maeng9newsfeed.domain.user.entity.User;
import com.sparta.maeng9newsfeed.domain.user.repository.UserRepository;
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

        // 가입했었던이메일인지 확인
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("사용할 수 없는 email입니다.");
        }
        
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

        // 이미 탈퇴한 회원의 아이디로 로그인 할 시
        if (!user.isStatus()) {
            throw new IllegalArgumentException("이미 탈퇴한 회원입니다.");
        }

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

    @Transactional
    public String signout(long userId, SignoutRequest signoutRequest) {

        User authuser = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("존재하지 않은 아이디입니다"));

        if (!passwordEncoder.matches(signoutRequest.getPassword(), authuser.getPassword())) {

            return "비밀 번호가 일치하지 않습니다";
        }

        //회원 탈퇴 처리
        authuser.setStatus(false);
        userRepository.save(authuser);

        return "회원 탈퇴 완료";

    }
}
