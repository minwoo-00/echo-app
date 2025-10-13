package com.echo.echo_backend.track.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter @Setter
public class Comment {
    private Long userId;
    private String userNickname;
    private String trackId;
    private String content;
    private LocalDateTime createdAt;

    public Comment(Long userId, String userNickname, String trackId, String content, LocalDateTime createdAt) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.trackId = trackId;
        this.content = content;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Comment comment = (Comment) object;
        return Objects.equals(userId, comment.userId) && Objects.equals(trackId, comment.trackId) && Objects.equals(createdAt, comment.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, trackId, createdAt);
    }
}
