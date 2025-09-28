package com.echo.echo_backend.user.entity;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class User {

    private Long id;
    private String spotify_id;
    private String email;
    private String nickname;
    private String profileMassage;
    private int followerCnt;
    private int followingCnt;
    private int rateCnt;
    private double avgRate;

    public User(String spotify_id, String email) {
        this.spotify_id = spotify_id;
        this.email = email;
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
}
