package com.sparta.maeng9newsfeed.domain.auth.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequest {

    private String userName;
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{3,10}$",
            message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$",
            message = "비밀번호는 대문자, 소문자, 숫자, 특수문자를 최소 1개씩 포함해야 하며, 최소 8자 이상이어야 합니다.")
    private String password;
}
