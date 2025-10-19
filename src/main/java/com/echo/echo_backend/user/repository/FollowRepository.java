package com.echo.echo_backend.user.repository;

import com.echo.echo_backend.user.entity.Follow;
import com.echo.echo_backend.user.entity.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowId> {

    Follow save(Follow follow);

    // 팔로잉 목록
    List<Follow> findByIdUserId(Long userId);

    // 팔로워 목록
    List<Follow> findByIdFollowingId(Long followingId);

    // 특정 팔로우 관계 존재 여부 확인
    boolean existsByIdUserIdAndIdFollowingId(Long userId, Long followingId);

    // 특정 팔로우 관계 삭제
    void deleteByIdUserIdAndIdFollowingId(Long userId, Long followingId);

    // 팔로워 수
    int countByIdFollowingId(Long userId);

    // 팔로잉 수
    int countByIdUserId(Long userId);

    /*int countFollowing(Long userId);
    int countFollower(Long userId);
    Follow save(Long userId, Long followingId);
    void delete(Long userId, Long followingId);
    List<Follow> findByFollowerId(Long followerId);
    List<Follow> findByFollowingId(Long followingId);*/
}
