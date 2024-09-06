package com.sparta.maeng9newsfeed.domain.friend.service;

import com.sparta.maeng9newsfeed.domain.friend.dto.request.FriendRequest;
import com.sparta.maeng9newsfeed.domain.friend.dto.response.FriendResponse;
import com.sparta.maeng9newsfeed.domain.friend.entity.Friend;
import com.sparta.maeng9newsfeed.domain.friend.entity.FriendDemand;
import com.sparta.maeng9newsfeed.domain.user.entity.User;
import com.sparta.maeng9newsfeed.domain.friend.repository.FriendDemandRepository;
import com.sparta.maeng9newsfeed.domain.friend.repository.FriendRepository;
import com.sparta.maeng9newsfeed.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendService {

    private final FriendRepository friendRepository;
    private final FriendDemandRepository friendDemandRepository;
    private final UserRepository userRepository;

    /**
     * 친구 추가 요청
     *
     * @param senderId      요청 사용자 ID
     * @param friendRequest 친구 추가할 사용자 ID를 담은 DTO
     * @return 성공 시 "친구 요청 완료"
     */
    @Transactional
    public String demandFriend(Long senderId, FriendRequest friendRequest) {
        User sender = findUserById(senderId);
        User receiver = findUserById(friendRequest.getUserId());
        // 본인에게 한 신청인지 확인
        if (receiver.getId().equals(sender.getId())) {
            throw new RuntimeException("본인에게 친구신청을 할 수 없습니다.");
        }
        // 중복 신청 확인
        if (friendDemandRepository.findBySender_IdAndReceiver_Id(senderId, receiver.getId()).isPresent()) {
            throw new RuntimeException("해당 친구 요청이 이미 존재합니다.");
        }
        // 친구신청한 사용자에게 친구신청을 받았는지 확인
        if (friendDemandRepository.findBySender_IdAndReceiver_Id(receiver.getId(), senderId).isPresent()) {
            throw new RuntimeException("해당 사용자에게 친구 요청을 받았습니다.");
        }
        // 이미 친구인지 확인
        if (friendRepository.findBySender_IdAndReceiver_Id(senderId, receiver.getId()).isPresent()) {
            throw new RuntimeException("해당 사용자와 이미 친구입니다.");
        }
        // 친구 요청 추가
        friendDemandRepository.save(new FriendDemand(sender, receiver));
        return "친구 요청 완료";
    }


    /**
     * 친구 요청 수락
     *
     * @param receiverId    요청을 받은 사용자 ID (수락 당사자 ID)
     * @param friendRequest 요청을 보내온 사용자 ID를 담은 DTO
     * @return 성공 시 "친구 수락 완료"
     */
    @Transactional
    public String acceptFriend(Long receiverId, FriendRequest friendRequest) {
        User sender = findUserById(friendRequest.getUserId());
        User receiver = findUserById(receiverId);
        // 해당 요청 유무 확인 후 친구요청 제거
        friendDemandRepository.findBySender_IdAndReceiver_Id(sender.getId(), receiverId)
                .ifPresentOrElse(friendDemandRepository::delete,    // 해당 친구 요청이 있으면 -> 요청 목록에서 삭제
                        () -> {
                            throw new RuntimeException("해당 친구요청이 존재하지 않습니다.");
                        });  // 없으면 -> 예외 발생
        // 친구 목록 데이블에 양방향 저장(보낸 경우, 받은 경우)
        friendRepository.save(new Friend(sender, receiver));
        friendRepository.save(new Friend(receiver, sender));
        return "친구 수락 완료";
    }

    /**
     * 친구 요청 거절
     *
     * @param receiverId    요청을 받은 사용자 ID (거절 당사자 ID)
     * @param friendRequest 요청을 보내온 사용자 ID를 담은 DTO
     * @return 성공 시 "친구 거절 완료"
     */
    @Transactional
    public String rejectFriend(Long receiverId, FriendRequest friendRequest) {
        User sender = findUserById(friendRequest.getUserId());
        friendDemandRepository.findBySender_IdAndReceiver_Id(sender.getId(), receiverId)
                .ifPresentOrElse(friendDemandRepository::delete, () -> {    // 해당 친구 요청이 있으면 -> 요청 목록에서 삭제
                    throw new RuntimeException("해당 친구요청이 존재하지 않습니다.");
                });  // 없으면 -> 예외 발생
        return "친구 거절 완료";
    }

    /**
     * 보낸 친구 추가 요청 목록 조회
     *
     * @param userId : 사용자 ID
     * @return List<FriendResponse> : 보낸 친구추가요청 목록
     */
    public List<FriendResponse> getSendFriendList(Long userId) {
        return friendDemandRepository.findAllBySender_Id(userId)      // 사용자id를 받은친구로 조회
                .map(list -> list.stream()
                        .map(FriendDemand::toReceiverResponse)  // 각 FriendDemand를 FriendResponse로 변환(receiver 사용자만 반환)
                        .collect(Collectors.toList()))  // 변환된 FriendResponse 객체들을 리스트로 수집
                .orElseThrow(() -> new RuntimeException("보낸 친구신청이 없습니다."));
    }

    /**
     * 받은 친구 추가 요청 목록 조회
     *
     * @param userId : 사용자 ID
     * @return List<FriendResponse> : 받은 친구추가요청 목록
     */
    public List<FriendResponse> getReceiveFriendList(Long userId) {
        return friendDemandRepository.findAllByReceiver_Id(userId)
                .map(list -> list.stream()
                        .map(FriendDemand::toSenderResponse)  // (sender 사용자만 반환)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new RuntimeException("받은 친구신청이 없습니다."));
    }

    /**
     * 친구목록 조회
     *
     * @param userId : 사용자 ID
     * @return List<FriendResponse> : 친구 목록
     */
    public List<FriendResponse> getFriendList(Long userId) {
        return friendRepository.findAllBySender_Id(userId)
                .map(list -> list.stream()
                        .map(Friend::toReceiverResponse)// (receiver 사용자만 반환)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new RuntimeException("친구 목록이 없습니다."));
    }

    /**
     * 친구 삭제
     *
     * @param senderId      : 사용자 ID
     * @param friendRequest : 삭제할 친구(사용자) ID를 담은 RequestDto
     * @return 성공 시 "친구 삭제 완료"
     */
    @Transactional
    public String deleteFriend(Long senderId, FriendRequest friendRequest) {
        User receiver = userRepository.findById(friendRequest.getUserId()).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 친구 목록 데이블에서 양방향 삭제(보낸 경우, 받은 경우)
        Stream.of(
                        friendRepository.findBySender_IdAndReceiver_Id(senderId, receiver.getId()),
                        friendRepository.findBySender_IdAndReceiver_Id(receiver.getId(), senderId)
                )
                .map(optionalFriend -> optionalFriend.orElseThrow(() -> new RuntimeException("친구관계가 아닙니다.")))
                .forEach(friendRepository::delete);
        return "친구 삭제 완료";
    }

    /**
     * 매일 00:00에 3일이 지난 친구 요청들을 자동 거절(삭제)
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 메소드 실행
    public void renewFriendDemandList() {
        LocalDateTime expirationDate = LocalDateTime.now().minusDays(3); // 3일이 지난 친구 요청
        friendDemandRepository.deleteAllByCreatedAtBefore(expirationDate);
    }

    /**
     * 사용자 id로 해당 사용자 객체 조회 및 반환
     *
     * @param userId : 찾을 사용자 id
     * @return : 찾은 사용자 객체(User)
     */
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));
    }
}

