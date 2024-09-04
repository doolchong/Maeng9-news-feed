package com.sparta.maeng9newsfeed.controller;

import com.sparta.maeng9newsfeed.annotation.Auth;
import com.sparta.maeng9newsfeed.dto.AuthUser;
import com.sparta.maeng9newsfeed.dto.FriendRequest;
import com.sparta.maeng9newsfeed.dto.FriendResponse;
import com.sparta.maeng9newsfeed.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/friends")
public class FriendController {

    private final FriendService friendService;

    @PostMapping
    public ResponseEntity<String> demandFriend(@Auth AuthUser authUser,
                                               @RequestBody FriendRequest friendRequest) {
        Long userId = authUser.getId();
        return ResponseEntity.ok(friendService.demandFriend(userId, friendRequest));
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriend(@Auth AuthUser authUser,
                                               @RequestBody FriendRequest friendRequest) {
        Long userId = authUser.getId();
        return ResponseEntity.ok(friendService.acceptFriend(userId, friendRequest));
    }

    @PostMapping("/reject")
    public ResponseEntity<String> rejectFriend(@Auth AuthUser authUser, @RequestBody FriendRequest friendRequest) {
        Long userId = authUser.getId();
        return ResponseEntity.ok(friendService.rejectFriend(userId, friendRequest));
    }

    @GetMapping("/senders")
    public ResponseEntity<List<FriendResponse>> getSendFriendList(@Auth AuthUser authUser) {
        Long userId = authUser.getId();
        return ResponseEntity.ok(friendService.getSendFriendList(userId));
    }

    @GetMapping("/receivers")
    public ResponseEntity<List<FriendResponse>> getReceiveFriendList(@Auth AuthUser authUser) {
        Long userId = authUser.getId();
        return ResponseEntity.ok(friendService.getReceiveFriendList(userId));
    }

    @GetMapping
    public ResponseEntity<List<FriendResponse>> getFriendList(@Auth AuthUser authUser) {
        Long userId = authUser.getId();
        return ResponseEntity.ok(friendService.getFriendList(userId));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFriend(@Auth AuthUser authUser, @RequestBody FriendRequest friendRequest) {
        Long userId = authUser.getId();
        return ResponseEntity.ok(friendService.deleteFriend(userId, friendRequest));
    }
}
