package com.sparta.maeng9newsfeed.domain.auth.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LogoutResponse {

    private final int httpStatusCode;
    private final String message;
}
