package com.echo.echo_backend.user.repository;

import com.echo.echo_backend.user.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Follow save(Follow follow);

    // 팔로잉 목록
    List<Follow> findByUserId(Long userId);

    // 팔로워 목록
    List<Follow> findByFollowingId(Long followingId);

    // 특정 팔로우 관계 존재 여부 확인
    boolean existsByUserIdAndFollowingId(Long userId, Long followingId);

    // 특정 팔로우 관계 삭제
    void deleteByUserIdAndFollowingId(Long userId, Long followingId);

    // 팔로워 수
    int countByFollowingId(Long userId);

    // 팔로잉 수
    int countByUserId(Long userId);

    /*int countFollowing(Long userId);
    int countFollower(Long userId);
    Follow save(Long userId, Long followingId);
    void delete(Long userId, Long followingId);
    List<Follow> findByFollowerId(Long followerId);
    List<Follow> findByFollowingId(Long followingId);*/
}
