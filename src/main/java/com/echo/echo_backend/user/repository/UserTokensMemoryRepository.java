package com.echo.echo_backend.user.repository;

import com.echo.echo_backend.user.entity.UserTokens;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class UserTokensMemoryRepository {

    private static final Map<String, UserTokens> storeTokens = new HashMap<>(); // key = spotify_id

    public UserTokens findTokenBySpotifyId(String spotify_id) {
        return storeTokens.get(spotify_id);
    }

    public UserTokens saveTokens(UserTokens userTokens) {
        storeTokens.put(userTokens.getSpotifyId(), userTokens);
        return userTokens;
    }

    public UserTokens updateTokens(String spotify_id, String accessToken, String refreshToken, int expiresIn) {
        UserTokens userTokens = storeTokens.get(spotify_id);
        userTokens.setSpotifyAccessToken(accessToken);
        userTokens.setSpotifyRefreshToken(refreshToken);
        userTokens.setExpiresAt(Instant.now().plusSeconds(expiresIn));
        return userTokens;
    }
}
