package com.sparta.maeng9newsfeed.controller;

import com.sparta.maeng9newsfeed.annotation.Auth;
import com.sparta.maeng9newsfeed.dto.AuthUser;
import com.sparta.maeng9newsfeed.dto.UserResponse;
import com.sparta.maeng9newsfeed.dto.UserUpdateRequest;
import com.sparta.maeng9newsfeed.dto.UserUpdateResponse;
import com.sparta.maeng9newsfeed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


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
}
