package com.echo.echo_backend.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @Column(nullable = false, unique = true)
    private String spotifyId;

    @Column(nullable = false, length = 512)
    private String spotifyAccessToken;

    @Column(nullable = false, length = 512)
    private String spotifyRefreshToken;

    @Column(nullable = false)
    private Instant expiresAt;

    public UserTokens(String spotify_id, String spotify_access_token, String spotify_refresh_token, int expiresIn) {
        this.spotifyId = spotify_id;
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
