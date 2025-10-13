package com.echo.echo_backend.track.repository;

import com.echo.echo_backend.track.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    Comment update(Comment newComment);
    Comment delete(Comment delComment);
    List<Comment> findByTrackId(String trackId);
    Optional<Comment> findByUserAndTrack(Long userId, String trackId);
}
