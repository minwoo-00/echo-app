package com.echo.echo_backend.track.dto;

import com.echo.echo_backend.track.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter @Setter
public class CommentDto {
    private Long id;
    private Long userId;
    private String userNickname;
    private String trackId;
    private String content;
    private LocalDateTime createdAt;

    public static CommentDto fromEntity(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .userId(comment.getUserId())
                .userNickname(comment.getUserNickname())
                .trackId(comment.getTrackId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
