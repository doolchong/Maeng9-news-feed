package com.sparta.maeng9newsfeed.domain.user.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserUpdateResponse {

    private final String userName;
    private final String email;
    private final String intro;


}


