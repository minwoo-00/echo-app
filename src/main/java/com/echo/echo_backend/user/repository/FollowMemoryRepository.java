package com.echo.echo_backend.user.repository;

import com.echo.echo_backend.user.entity.Follow;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FollowMemoryRepository {

    private static final Map<Long, Follow> store = new HashMap<>();
    private static long idSequence = 0L;


    /*public Follow save(Long userId, Long followingId) {
        Follow follow = new Follow(++idSequence, userId, followingId);
        store.put(follow.getSeqId(), follow);
        return follow;
    }*/

    public void delete(Long userId, Long followingId) {
        store.values().removeIf(f ->
                f.getUserId().equals(userId) &&
                        f.getFollowingId().equals(followingId)
        );
    }

    public int countFollowing(Long userId) { // 내가 팔로잉 하는 사람 수
        return findByFollowerId(userId).size();
    }

    public int countFollower(Long userId) { // 내 팔로워 수
        return findByFollowingId(userId).size();
    }

    public List<Follow> findByFollowerId(Long followerId) {
        return store.values().stream()
                .filter(f -> f.getUserId().equals(followerId))
                .toList();
    }
    public List<Follow> findByFollowingId(Long followingId) {
        return store.values().stream()
                .filter(f -> f.getFollowingId().equals(followingId))
                .toList();
    }
}
