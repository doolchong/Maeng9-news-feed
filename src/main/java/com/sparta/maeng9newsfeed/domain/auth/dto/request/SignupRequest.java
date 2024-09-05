package com.sparta.maeng9newsfeed.domain.auth.dto.request;

import lombok.Getter;

@Getter
public class SignupRequest {

    private String userName;
    private String email;
    private String password;
}
