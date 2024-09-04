package com.sparta.maeng9newsfeed.entity;

import com.sparta.maeng9newsfeed.dto.SignupRequest;
import com.sparta.maeng9newsfeed.dto.UserUpdateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor

public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(nullable = false)
    private String userName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String intro;
    private boolean status;     // 1. true : 회원가입 상태 2. false : 회원탈퇴 상태

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<BoardLike> boardLikeList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    public User(SignupRequest signupRequest) {
        userName = signupRequest.getUserName();
        email = signupRequest.getEmail();
        password = signupRequest.getPassword();
        intro = "한 줄 소개를 입력해주세요";
        status = true;
    }

    public void update(UserUpdateRequest userUpdateRequest) {
        userName = userUpdateRequest.getUserName();
        email = userUpdateRequest.getEmail();
        intro = userUpdateRequest.getIntro();
    }
}

