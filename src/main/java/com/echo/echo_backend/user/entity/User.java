package com.echo.echo_backend.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String spotifyId;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(length = 50)
    private String nickname;

    @Column(length = 255)
    private String profileMessage;

    @Column(length = 500)
    private String profileImageUrl;

    @Column(nullable = false)
    private int followerCnt = 0;

    @Column(nullable = false)
    private int followingCnt = 0;

    @Column(nullable = false)
    private int rateCnt = 0;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserTokens tokens;

   /* @Column(nullable = false) // 삭제 예정
    private double avgRate = 0.0;*/

    public User(String spotifyId, String email) {
        this.spotifyId = spotifyId;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }

    public void incrementFollowing() {
        this.followingCnt++;
    }

    public void decrementFollowing() {
        if (followingCnt > 0) {
            this.followingCnt--;
        }
    }

    public void incrementFollower() {
        this.followerCnt++;
    }

    public void decrementFollower() {
        if (followerCnt > 0) {
            this.followerCnt--;
        }
    }

    public void incrementRateCnt() {
        this.rateCnt++;
    }

    public void decrementRateCnt() {
        if (rateCnt > 0) {
            this.rateCnt--;
        }
    }

}
