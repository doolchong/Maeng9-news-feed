package com.sparta.maeng9newsfeed.domain.friend.repository;

import com.sparta.maeng9newsfeed.domain.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<List<Friend>> findAllBySender_Id(Long senderId);

    Optional<Friend> findBySender_IdAndReceiver_Id(Long senderId, Long receiverId);
}
