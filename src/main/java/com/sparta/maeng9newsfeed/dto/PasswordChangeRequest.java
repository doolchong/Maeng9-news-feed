package com.sparta.maeng9newsfeed.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PasswordChangeRequest {

    private final String password;
    private final String newPassword;
}
