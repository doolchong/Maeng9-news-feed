package com.sparta.maeng9newsfeed.service;

import com.sparta.maeng9newsfeed.dto.FriendRequest;
import com.sparta.maeng9newsfeed.dto.FriendResponse;
import com.sparta.maeng9newsfeed.entity.Friend;
import com.sparta.maeng9newsfeed.entity.FriendDemand;
import com.sparta.maeng9newsfeed.entity.User;
import com.sparta.maeng9newsfeed.repository.FriendDemandRepository;
import com.sparta.maeng9newsfeed.repository.FriendRepository;
import com.sparta.maeng9newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendService {

    private final FriendRepository friendRepository;
    private final FriendDemandRepository friendDemandRepository;
    private final UserRepository userRepository;

    /**
     * 친구 추가 요청
     * @param senderId 요청 사용자 ID
     * @param friendRequest 친구 추가할 사용자 ID를 담은 DTO
     * @return 성공 시 "친구 요청 완료"
     */
    @Transactional
    public String demandFriend(Long senderId, FriendRequest friendRequest) {
        User sender = userRepository.findById(senderId).orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
        User receiver = userRepository.findById(friendRequest.getUserId()).orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 중복 신청 확인
        if(friendDemandRepository.findBySender_IdAndReceiver_Id(senderId, receiver.getId()).isPresent()){
            throw new RuntimeException("해당 친구 요청이 이미 존재합니다.");
        }
        // 친구신청한 사용자에게 친구신청을 받았는지 확인
        if(friendDemandRepository.findByReceiver_Id(receiver.getId()).isPresent()){
            throw new RuntimeException("해당 사용자에게 친구 요청을 받았습니다.");
        }
        // 친구 추가
        friendDemandRepository.save(new FriendDemand(sender, receiver));
        return "친구 요청 완료";
    }

    /**
     * 친구 요청 수락
     * @param senderId 요청을 받은 사용자 ID (수락 당사자 ID)
     * @param friendRequest 요청을 보내온 사용자 ID를 담은 DTO
     * @return 성공 시 "친구 수락 완료"
     */
    @Transactional
    public String acceptFriend(Long senderId, FriendRequest friendRequest) {
        User sender = userRepository.findById(senderId).orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
        User receiver = userRepository.findById(friendRequest.getUserId()).orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 해당 요청 유무 확인 후 친구요청 제거
        friendDemandRepository.findBySender_IdAndReceiver_Id(senderId, receiver.getId()).ifPresent(friendDemandRepository::delete);
        // 친구 목록에 양방향 저장
        friendRepository.save(new Friend(sender, receiver));
        friendRepository.save(new Friend(receiver, sender));
        return "친구 수락 완료";
    }

    /**
     * 친구 요청 거절
     * @param senderId 요청을 받은 사용자 ID (거절 당사자 ID)
     * @param friendRequest 요청을 보내온 사용자 ID를 담은 DTO
     * @return 성공 시 "친구 거절 완료"
     */
    @Transactional
    public String rejectFriend(Long senderId, FriendRequest friendRequest) {
        User receiver = userRepository.findById(friendRequest.getUserId()).orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
        friendDemandRepository.findBySender_IdAndReceiver_Id(senderId, receiver.getId())
                .ifPresent(friendDemandRepository::delete);     // 해당 친구 요청이 있으면 -> 요청 목록에서 삭제
        return "친구 거절 완료";
    }

    /**
     * 보낸 친구 추가 요청 목록 조회
     * @param userId : 사용자 ID
     * @return List<FriendResponse> : 보낸 친구추가요청 목록
     */
    public List<FriendResponse> getSendFriendList(Long userId) {
        return friendDemandRepository.findBySender_Id(userId)
                .map(friendDemand -> Collections.singletonList(friendDemand.toResponse()))
                .orElseThrow(() -> new RuntimeException("보낸 친구 추가 요청이 없습니다."));
    }

    /**
     * 받은 친구 추가 요청 목록 조회
     * @param userId : 사용자 ID
     * @return List<FriendResponse> : 받은 친구추가요청 목록
     */
    public List<FriendResponse> getReceiveFriendList(Long userId) {
        return friendDemandRepository.findByReceiver_Id(userId)
                .map(friendDemand -> Collections.singletonList(friendDemand.toResponse()))
                .orElseThrow(() -> new RuntimeException("받은 친구 추가 요청이 없습니다."));
    }

    /**
     * 친구목록 조회
     * @param userId : 사용자 ID
     * @return List<FriendResponse> : 친구 목록
     */
    public List<FriendResponse> getFriendList(Long userId) {
         return friendRepository.findBySender_Id(userId)
                .map(friend -> Collections.singletonList(friend.toResponse()))
                .orElseThrow(()->new RuntimeException("친구 목록이 없습니다."));
    }

    /**
     * 친구 삭제
     * @param senderId : 사용자 ID
     * @param friendRequest : 삭제할 친구(사용자) ID를 담은 RequestDto
     * @return 성공 시 "친구 삭제 완료"
     */
    @Transactional
    public String deleteFriend(Long senderId, FriendRequest friendRequest) {
        User receiver = userRepository.findById(friendRequest.getUserId()).orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
        friendRepository.findBySender_IdAndReceiver_Id(senderId, receiver.getId())
                .ifPresent(friendRepository::delete);
        return "친구 삭제 완료";
    }

}
