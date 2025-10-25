package com.echo.echo_backend.badge.entity;

import com.echo.echo_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_badge")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBadge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Badge badge;

    @Column(nullable = false)
    private String aiComment;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
