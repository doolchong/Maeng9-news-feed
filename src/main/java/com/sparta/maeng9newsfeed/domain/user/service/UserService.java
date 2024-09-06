package com.sparta.maeng9newsfeed.domain.user.service;

import com.sparta.maeng9newsfeed.config.PasswordEncoder;
import com.sparta.maeng9newsfeed.domain.friend.dto.response.FriendResponse;
import com.sparta.maeng9newsfeed.domain.friend.service.FriendService;
import com.sparta.maeng9newsfeed.domain.user.dto.request.PasswordChangeRequest;
import com.sparta.maeng9newsfeed.domain.user.dto.request.UserUpdateRequest;
import com.sparta.maeng9newsfeed.domain.user.dto.response.UserResponse;
import com.sparta.maeng9newsfeed.domain.user.dto.response.UserUpdateResponse;
import com.sparta.maeng9newsfeed.domain.user.entity.User;
import com.sparta.maeng9newsfeed.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FriendService friendService;

    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NullPointerException("해당 사용자를 찾을 수 없습니다."));
        List<FriendResponse> friendList = friendService.getFriendList(userId);
        int followrCount = friendList.size();
        return UserResponse.userResponse(user, followrCount);
    }

    @Transactional
    public UserUpdateResponse updateUser(long userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("해당 사용자를 찾을 수 없습니다."));

        user.update(userUpdateRequest);

        return new UserUpdateResponse(user.getUserName(), user.getEmail(), userUpdateRequest.getIntro());

    }

    public User findByUserId(long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다.")
                );
    }

    @Transactional
    public void updatePassword(long userId, PasswordChangeRequest passwordChangeRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NullPointerException("해당 사용자를 찾을 수 없습니다."));
        // 사용자 비밀번호 인증
        if (!passwordEncoder.matches(passwordChangeRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
        }
        user.updatePaswword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        userRepository.save(user);
    }
}
