package com.sparta.maeng9newsfeed.dto;

import com.sparta.maeng9newsfeed.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserResponse {

    private final String userName;
    private final String email;
    private final String intro;
    private final Integer followers;


    public static UserResponse userResponse(User user, int followerCount) {
        return new UserResponse(
                user.getUserName(),
                user.getEmail(),
                user.getIntro(),
                followerCount
        );
    }
}
