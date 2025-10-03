package com.echo.echo_backend.user.repository;

import com.echo.echo_backend.user.entity.UserTokens;

public interface UserTokensRepository {
    UserTokens saveTokens(String spotify_id, String accessToken, String refreshToken, int expiresIn);
    UserTokens updateTokens(String spotify_id, String accessToken, String refreshToken, int expiresIn);
    UserTokens findTokenBySpotifyId(String spotify_id);
}
