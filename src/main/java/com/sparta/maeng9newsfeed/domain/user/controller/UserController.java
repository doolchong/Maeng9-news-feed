package com.sparta.maeng9newsfeed.domain.user.controller;

import com.sparta.maeng9newsfeed.annotation.Auth;
import com.sparta.maeng9newsfeed.common.dto.AuthUser;
import com.sparta.maeng9newsfeed.domain.user.dto.request.PasswordChangeRequest;
import com.sparta.maeng9newsfeed.domain.user.dto.request.UserUpdateRequest;
import com.sparta.maeng9newsfeed.domain.user.dto.response.UserResponse;
import com.sparta.maeng9newsfeed.domain.user.dto.response.UserUpdateResponse;
import com.sparta.maeng9newsfeed.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public UserResponse getUser(@Auth AuthUser authUser) {
        Long userId = authUser.getId();
        return userService.getUser(userId);
    }

    @PutMapping("/users")
    public UserUpdateResponse updateUser(@Auth AuthUser authUser,
                                         @RequestBody UserUpdateRequest userUpdateRequest) {
        Long userId = authUser.getId();

        return userService.updateUser(userId, userUpdateRequest);
    }

    @PatchMapping("/password")
    public ResponseEntity<String> updatePassword(@Auth AuthUser authUser,
                                                 @RequestBody @Valid PasswordChangeRequest passwordChangeRequest, BindingResult result) {
        if (result.hasErrors()) {
            // 검증 실패 시 첫 번째 오류 메시지 반환
            return ResponseEntity.badRequest().body(result.getAllErrors().get(0).getDefaultMessage());
        }
        Long userId = authUser.getId();
        userService.updatePassword(userId, passwordChangeRequest);

        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }
}
