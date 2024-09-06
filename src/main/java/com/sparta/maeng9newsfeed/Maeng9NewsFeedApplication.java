package com.sparta.maeng9newsfeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class Maeng9NewsFeedApplication {

    public static void main(String[] args) {
        SpringApplication.run(Maeng9NewsFeedApplication.class, args);
    }

}
