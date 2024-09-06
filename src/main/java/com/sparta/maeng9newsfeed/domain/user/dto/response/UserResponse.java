package com.sparta.maeng9newsfeed.domain.user.dto.response;

import com.sparta.maeng9newsfeed.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserResponse {

    private final String userName;
    private final String email;
    private final String intro;
    private final Integer friends;


    public static UserResponse userResponse(User user, int followerCount) {
        return new UserResponse(
                user.getUserName(),
                user.getEmail(),
                user.getIntro(),
                followerCount
        );
    }
}
