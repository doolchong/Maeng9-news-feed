package com.sparta.maeng9newsfeed.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private String userName;
    private String email;
    private String intro;
}
