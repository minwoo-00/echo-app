package com.echo.echo_backend.track.repository;

import com.echo.echo_backend.track.entity.Comment;
import com.echo.echo_backend.track.entity.Rating;
import org.springframework.stereotype.Repository;

import java.util.*;

public class CommentMemoryRepository {
    private static final Set<Comment> store = new HashSet<>();

    public Comment save(Comment comment) {
        store.add(comment);
        return comment;
    }

    public Comment update(Comment newComment) {
        for (Comment comment : store) {
            if (comment.equals(newComment)) {
                comment.setContent(newComment.getContent());
            }
        }
        return newComment;
    }

    public Comment delete(Comment delComment) {
        for (Comment comment : store) {
            if (comment.equals(delComment)) {
                store.remove(comment);
            }
        }
        return delComment;
    }

    public List<Comment> findByTrackId(String trackId) {
        return store.stream().filter(comment -> Objects.equals(comment.getTrackId(), trackId)).toList();
    }

    public Optional<Comment> findByUserAndTrack(Long userId, String trackId) {
        for (Comment comment : store) {
            if (comment.getUserId().equals(userId) && comment.getTrackId().equals(trackId)) {
                return Optional.of(comment);
            }
        }
        return Optional.empty();
    }
}
