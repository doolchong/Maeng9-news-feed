package com.sparta.maeng9newsfeed.repository;

import com.sparta.maeng9newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
