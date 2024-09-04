package com.sparta.maeng9newsfeed.service;

import com.sparta.maeng9newsfeed.config.PasswordEncoder;
import com.sparta.maeng9newsfeed.dto.PasswordChangeRequest;
import com.sparta.maeng9newsfeed.dto.UserResponse;
import com.sparta.maeng9newsfeed.dto.UserUpdateRequest;
import com.sparta.maeng9newsfeed.dto.UserUpdateResponse;
import com.sparta.maeng9newsfeed.entity.User;
import com.sparta.maeng9newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("해당 사용자를 찾을 수 없습니다."));
        return UserResponse.userResponse(user, 0);
    }

    @Transactional
    public UserUpdateResponse updateUser(long userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("해당 사용자를 찾을 수 없습니다."));

        user.update(userUpdateRequest);

        return new UserUpdateResponse(user.getUserName(), user.getEmail(), userUpdateRequest.getIntro());

    }

    public User findByUserId(long userId) {

        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다.")
        );
    }

    public void updatePassword(long userId, PasswordChangeRequest passwordChangeRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("해당 사용자를 찾을 수 없습니다."));

        // 사용자 비밀번호 인증
        if (!passwordEncoder.matches(passwordChangeRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
        }

        user.updatePaswword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        userRepository.save(user);

    }
}
