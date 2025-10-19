package com.echo.echo_backend.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "user_tokens")
public class UserTokens {

    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 512)
    private String spotifyAccessToken;

    @Column(nullable = false, length = 512)
    private String spotifyRefreshToken;

    @Column(nullable = false)
    private Instant expiresAt;

    public UserTokens(User user, String spotify_access_token, String spotify_refresh_token, int expiresIn) {
        this.user = user;
        this.userId = user.getId();
        this.spotifyAccessToken = spotify_access_token;
        this.spotifyRefreshToken = spotify_refresh_token;
        this.expiresAt = Instant.now().plusSeconds(expiresIn);
    }

    public void updateTokens(String accessToken, String refreshToken, int expiresIn) {
        this.spotifyAccessToken = accessToken;
        this.spotifyRefreshToken = refreshToken;
        this.expiresAt = Instant.now().plusSeconds(expiresIn);
    }
}
