package com.echo.echo_backend.badge.repository;

import com.echo.echo_backend.badge.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    // 유저의 뱃지 히스토리 목록
    List<UserBadge> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 유저의 현재 뱃지
    Optional<UserBadge> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
