package com.echo.echo_backend.user.entity;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Follow {

    private Long seqId;
    private Long userId; // 팔로우 하는 사람
    private Long followingId; // 팔로우 당하는 사람

    public Follow(Long seqId, Long userId, Long followingId) {
        this.seqId = seqId;
        this.userId = userId;
        this.followingId = followingId;
    }
}
