package com.sparta.maeng9newsfeed.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserUpdateResponse {

    private final String userName;
    private final String email;
    private final String intro;


}


