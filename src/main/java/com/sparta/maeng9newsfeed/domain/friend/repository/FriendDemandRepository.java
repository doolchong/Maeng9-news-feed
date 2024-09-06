package com.sparta.maeng9newsfeed.domain.friend.repository;

import com.sparta.maeng9newsfeed.domain.friend.entity.FriendDemand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FriendDemandRepository extends JpaRepository<FriendDemand, Long> {
    Optional<FriendDemand> findBySender_IdAndReceiver_Id(Long senderId, Long receiverId);

    Optional<List<FriendDemand>> findAllBySender_Id(Long senderId);

    Optional<List<FriendDemand>> findAllByReceiver_Id(Long receiverId);

    void deleteAllByCreatedAtBefore(LocalDateTime expirationDate);
}
