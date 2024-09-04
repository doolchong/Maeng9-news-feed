package com.sparta.maeng9newsfeed.entity;

import com.sparta.maeng9newsfeed.config.PasswordEncoder;
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boardList;

    public User(String userName, String email, String password, String intro, boolean status) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.intro = intro;
        this.status = status;
    }

    public void update(UserUpdateRequest userUpdateRequest) {
        userName = userUpdateRequest.getUserName();
        email = userUpdateRequest.getEmail();
        intro = userUpdateRequest.getIntro();
    }

    public void updatePaswword(String password) {
        this.password = password;
    }
}

