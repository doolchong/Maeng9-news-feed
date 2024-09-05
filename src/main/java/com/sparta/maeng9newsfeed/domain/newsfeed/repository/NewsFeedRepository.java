package com.sparta.maeng9newsfeed.domain.newsfeed.repository;

import com.sparta.maeng9newsfeed.domain.newsfeed.entity.NewsFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NewsFeedRepository extends JpaRepository<NewsFeed, Long> {

    List<NewsFeed> findTop10ByBoard_User_IdInOrderByBoardModifiedAt(List<Long> user_id);

    void deleteAllByBoardCreatedAtBefore(LocalDateTime expirationDate);
}
