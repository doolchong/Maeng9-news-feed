package com.sparta.maeng9newsfeed.controller;

import com.sparta.maeng9newsfeed.annotation.Auth;
import com.sparta.maeng9newsfeed.dto.*;
import com.sparta.maeng9newsfeed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public UserUpdateResponse updateUser(@Auth AuthUser authUser, @RequestBody UserUpdateRequest userUpdateRequest) {
        Long userId = authUser.getId();
        return userService.updateUser(userId, userUpdateRequest);
    }

    @PatchMapping("/password")
    public ResponseEntity<String> updatePassword(@Auth AuthUser authUser,
                                                 @RequestBody PasswordChangeRequest passwordChangeRequest) {
        Long userId = authUser.getId();
        userService.updatePassword(userId, passwordChangeRequest);
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }
}
