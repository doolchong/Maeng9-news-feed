package com.sparta.maeng9newsfeed.service;

import com.sparta.maeng9newsfeed.dto.BoardResponse;
import com.sparta.maeng9newsfeed.entity.Board;
import com.sparta.maeng9newsfeed.entity.Friend;
import com.sparta.maeng9newsfeed.entity.NewsFeed;
import com.sparta.maeng9newsfeed.repository.FriendRepository;
import com.sparta.maeng9newsfeed.repository.NewsFeedRepository;
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
        List<Friend> friendList = friendRepository.findAllBySender_Id(userId);
        List<Long> friendIdList = friendList.stream().map(friend -> friend.getReceiver().getId()).toList();
        List<Board> boardList = newsFeedRepository.findTop10ByBoard_User_IdInOrderByBoardModifiedAt(friendIdList).stream().map(NewsFeed::getBoard).toList();
        for (Board board : boardList) {
            System.out.println(board.getUser().getId());
        }
        return boardList.stream().map(BoardResponse::new).toList();
    }

    @Transactional
    @Scheduled(cron = "0 0/1 * * * ?") // 나중에 한 시간으로 수정하기
    public void renewNewsFeed() {
        LocalDateTime expirationDate = LocalDateTime.now().minusHours(1); // 1시간 지난 데이터
        newsFeedRepository.deleteAllByBoardCreatedAtBefore(expirationDate);
    }

    @Transactional
    public void create(Board saveBoard) {
        NewsFeed newsFeed = new NewsFeed(saveBoard);
        newsFeedRepository.save(newsFeed);
    }
}
