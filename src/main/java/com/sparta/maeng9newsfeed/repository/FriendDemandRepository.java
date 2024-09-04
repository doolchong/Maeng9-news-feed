package com.sparta.maeng9newsfeed.repository;

import com.sparta.maeng9newsfeed.entity.FriendDemand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendDemandRepository extends JpaRepository<FriendDemand, Long> {
    Optional<FriendDemand> findByReceiver_Id(Long receiverId);
    Optional<FriendDemand> findBySender_IdAndReceiver_Id(Long senderId, Long receiverId);
    Optional<List<FriendDemand>> findAllBySender_Id(Long senderId);
    Optional<List<FriendDemand>> findAllByReceiver_Id(Long receiverId);
}
