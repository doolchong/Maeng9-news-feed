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

import java.util.List;
import java.util.stream.Collectors;

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
     * @param receiverId 요청을 받은 사용자 ID (수락 당사자 ID)
     * @param friendRequest 요청을 보내온 사용자 ID를 담은 DTO
     * @return 성공 시 "친구 수락 완료"
     */
    @Transactional
    public String acceptFriend(Long receiverId, FriendRequest friendRequest) {
        User receiver = userRepository.findById(receiverId).orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
        User sender = userRepository.findById(friendRequest.getUserId()).orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 해당 요청 유무 확인 후 친구요청 제거
        friendDemandRepository.findBySender_IdAndReceiver_Id(sender.getId(), receiverId).ifPresent(friendDemandRepository::delete);
        // 친구 목록 데이블에 양방향 저장(보낸 경우, 받은 경우)
        friendRepository.save(new Friend(sender, receiver));
        friendRepository.save(new Friend(receiver, sender));
        return "친구 수락 완료";
    }

    /**
     * 친구 요청 거절
     * @param receiverId 요청을 받은 사용자 ID (거절 당사자 ID)
     * @param friendRequest 요청을 보내온 사용자 ID를 담은 DTO
     * @return 성공 시 "친구 거절 완료"
     */
    @Transactional
    public String rejectFriend(Long receiverId, FriendRequest friendRequest) {
        User sender = userRepository.findById(friendRequest.getUserId()).orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
        friendDemandRepository.findBySender_IdAndReceiver_Id(sender.getId(), receiverId)
                .ifPresent(friendDemandRepository::delete);     // 해당 친구 요청이 있으면 -> 요청 목록에서 삭제
        return "친구 거절 완료";
    }

    /**
     * 보낸 친구 추가 요청 목록 조회
     * @param userId : 사용자 ID
     * @return List<FriendResponse> : 보낸 친구추가요청 목록
     */
    public List<FriendResponse> getSendFriendList(Long userId) {
        return friendDemandRepository.findAllBySender_Id(userId)
                .filter(list -> !list.isEmpty())  // 리스트가 비어있지 않은지 확인
                .map(list -> list.stream()
                        .map(FriendDemand::toSenderResponse)  // 각 FriendDemand를 FriendResponse로 변환
                        .collect(Collectors.toList()))  // 변환된 FriendResponse 객체들을 리스트로 수집
                .orElseThrow(() -> new RuntimeException("보낸 친구신청이 없습니다."));
    }

    /**
     * 받은 친구 추가 요청 목록 조회
     * @param userId : 사용자 ID
     * @return List<FriendResponse> : 받은 친구추가요청 목록
     */
    public List<FriendResponse> getReceiveFriendList(Long userId) {
        return friendDemandRepository.findAllByReceiver_Id(userId)
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream()
                        .map(FriendDemand::toReceiverResponse)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new RuntimeException("받은 친구신청이 없습니다."));
    }

    /**
     * 친구목록 조회
     * @param userId : 사용자 ID
     * @return List<FriendResponse> : 친구 목록
     */
    public List<FriendResponse> getFriendList(Long userId) {
         return friendRepository.findAllBySender_Id(userId)
                 .filter(list -> !list.isEmpty())
                 .map(list -> list.stream()
                         .map(Friend::toReceiverResponse)
                         .collect(Collectors.toList()))
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
        // 친구 목록 데이블에서 양방향 삭제(보낸 경우, 받은 경우)
        friendRepository.findBySender_IdAndReceiver_Id(senderId, receiver.getId())
                .ifPresent(friendRepository::delete);
        friendRepository.findBySender_IdAndReceiver_Id(receiver.getId(), senderId)
                .ifPresent(friendRepository::delete);
        return "친구 삭제 완료";
    }

}
