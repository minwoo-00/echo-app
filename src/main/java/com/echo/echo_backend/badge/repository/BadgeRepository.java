package com.echo.echo_backend.badge.repository;

import com.echo.echo_backend.badge.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Optional<Badge> findByBadgeName(String badgeName);
}
