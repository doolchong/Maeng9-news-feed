package com.sparta.maeng9newsfeed.dto;

import com.sparta.maeng9newsfeed.entity.Friend;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendRequest {
    private Long userId;
}
