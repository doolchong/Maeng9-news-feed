package com.sparta.maeng9newsfeed.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LogoutResponse {

    private final int httpStatusCode;
    private final String message;
}
