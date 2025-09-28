package com.echo.echo_backend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponse {
    private Long id;
    private String nickname;
    private String profileMassage;
    private int followerCnt;
    private int followingCnt;
    private int rateCnt;
    private double avgRate;
    private boolean followState;

    public UserResponse(Long id, String nickname, String profileMassage, int followerCnt, int followingCnt, int rateCnt, double avgRate) {
        this.id = id;
        this.nickname = nickname;
        this.profileMassage = profileMassage;
        this.followerCnt = followerCnt;
        this.followingCnt = followingCnt;
        this.rateCnt = rateCnt;
        this.avgRate = avgRate;
    }

}
