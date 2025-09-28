package com.echo.echo_backend.user.repository;

import com.echo.echo_backend.user.entity.Follow;

import java.util.List;

public interface FollowRepository {
    int countFollowing(Long userId);
    int countFollower(Long userId);
    Follow save(Long userId, Long followingId);
    void delete(Long userId, Long followingId);
    List<Follow> findByFollowerId(Long followerId);
    List<Follow> findByFollowingId(Long followingId);
}
