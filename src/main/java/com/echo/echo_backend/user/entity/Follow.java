package com.echo.echo_backend.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "follows")
public class Follow {

    @EmbeddedId
    private FollowId id;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Follow(Long userId, Long followingId) {
        this.id = new FollowId(userId, followingId);
        createdAt = LocalDateTime.now();
    }

    public Long getUserId() {
        return id != null ? id.getUserId() : null;
    }

    public Long getFollowingId() {
        return id != null ? id.getFollowingId() : null;
    }
}
