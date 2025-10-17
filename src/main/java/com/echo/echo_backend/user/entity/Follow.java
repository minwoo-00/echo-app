package com.echo.echo_backend.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "follows")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seqId;

    @Column(nullable = false)
    private Long userId; // 팔로우 하는 사람

    @Column(nullable = false)
    private Long followingId; // 팔로우 당하는 사람

    public Follow(Long userId, Long followingId) {
        this.userId = userId;
        this.followingId = followingId;
    }
}
