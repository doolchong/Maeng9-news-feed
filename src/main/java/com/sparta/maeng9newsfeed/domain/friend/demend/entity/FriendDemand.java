package com.sparta.maeng9newsfeed.domain.friend.demend.entity;

import com.sparta.maeng9newsfeed.domain.friend.dto.response.FriendResponse;
import com.sparta.maeng9newsfeed.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "friend_demands")
public class FriendDemand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private final LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "user_id", name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "user_id", name = "receiver_id", nullable = false)
    private User receiver;

    public FriendDemand(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public FriendResponse toReceiverResponse() {
        return new FriendResponse(
                this.getReceiver().getUserName(),
                this.getReceiver().getEmail()
        );
    }

    public FriendResponse toSenderResponse() {
        return new FriendResponse(
                this.getSender().getUserName(),
                this.getSender().getEmail()
        );
    }
}
