package com.sparta.maeng9newsfeed.service;

import com.sparta.maeng9newsfeed.dto.UserResponse;
import com.sparta.maeng9newsfeed.entity.User;
import com.sparta.maeng9newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
  
    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("해당 사용자를 찾을 수 없습니다."));
        return UserResponse.userResponse(user, 0);
    }

    public User findByUserId(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다.")
        );
    }
}
