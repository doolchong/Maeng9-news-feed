package com.sparta.maeng9newsfeed.domain.newsfeed.service;

import com.sparta.maeng9newsfeed.domain.board.dto.BoardResponse;
import com.sparta.maeng9newsfeed.domain.board.entity.Board;
import com.sparta.maeng9newsfeed.domain.friend.entity.Friend;
import com.sparta.maeng9newsfeed.domain.newsfeed.entity.NewsFeed;
import com.sparta.maeng9newsfeed.domain.friend.repository.FriendRepository;
import com.sparta.maeng9newsfeed.domain.newsfeed.repository.NewsFeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsFeedService {

    private final FriendRepository friendRepository;
    private final NewsFeedRepository newsFeedRepository;

    public List<BoardResponse> getNewsFeed(long userId) {
        List<Friend> friendList = friendRepository.findAllBySender_Id(userId).orElseThrow(
                () -> new IllegalArgumentException("친구 목록이 없습니다."));
        List<Long> friendIdList = friendList.stream().map(friend -> friend.getReceiver().getId()).toList();
        List<Board> boardList = newsFeedRepository.findTop10ByBoard_User_IdInOrderByBoardModifiedAt(friendIdList).stream().map(NewsFeed::getBoard).toList();
        for (Board board : boardList) {
            System.out.println(board.getUser().getId());
        }
        return boardList.stream().map(BoardResponse::new).toList();
    }

    @Transactional
    @Scheduled(cron = "0 0/10 * * * ?")
    public void renewNewsFeed() {
        LocalDateTime expirationDate = LocalDateTime.now().minusHours(1); // 1시간 지난 게시글
        newsFeedRepository.deleteAllByBoardCreatedAtBefore(expirationDate);
    }

    @Transactional
    public void create(Board saveBoard) {
        NewsFeed newsFeed = new NewsFeed(saveBoard);
        newsFeedRepository.save(newsFeed);
    }
}
