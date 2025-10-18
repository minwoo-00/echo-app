package com.echo.echo_backend.track.repository;

import com.echo.echo_backend.track.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByTrackId(String trackId);

    Optional<Comment> findByUserIdAndTrackId(Long userId, String trackId);

    /*Comment save(Comment comment);
    Comment update(Comment newComment);
    Comment delete(Comment delComment);
    List<Comment> findByTrackId(String trackId);
    Optional<Comment> findByUserAndTrack(Long userId, String trackId);*/
}
