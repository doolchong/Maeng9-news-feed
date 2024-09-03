package com.sparta.maeng9newsfeed.controller;

import com.sparta.maeng9newsfeed.dto.UserResponse;
import com.sparta.maeng9newsfeed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public UserResponse getUser(@PathVariable long userId) {
        return userService.getUser(userId);
    }
}
