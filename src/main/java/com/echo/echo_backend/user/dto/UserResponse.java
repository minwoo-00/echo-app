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
    private boolean followState;
    private String profileImageUrl;

    public UserResponse(Long id, String nickname, String profileMassage, String profileImageUrl, int followerCnt, int followingCnt, int rateCnt) {
        this.id = id;
        this.nickname = nickname;
        this.profileMassage = profileMassage;
        this.profileImageUrl = profileImageUrl;
        this.followerCnt = followerCnt;
        this.followingCnt = followingCnt;
        this.rateCnt = rateCnt;
    }

}
