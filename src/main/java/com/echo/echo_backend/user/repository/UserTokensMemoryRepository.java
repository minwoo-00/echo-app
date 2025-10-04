package com.echo.echo_backend.user.repository;

import com.echo.echo_backend.user.entity.UserTokens;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserTokensMemoryRepository implements UserTokensRepository{

    private static final Map<String, UserTokens> storeTokens = new HashMap<>(); // key = spotify_id

    @Override
    public UserTokens findTokenBySpotifyId(String spotify_id) {
        return storeTokens.get(spotify_id);
    }

    @Override
    public UserTokens saveTokens(UserTokens userTokens) {
        storeTokens.put(userTokens.getSpotify_id(), userTokens);
        return userTokens;
    }

    @Override
    public UserTokens updateTokens(String spotify_id, String accessToken, String refreshToken, int expiresIn) {
        UserTokens userTokens = storeTokens.get(spotify_id);
        userTokens.setSpotify_access_token(accessToken);
        userTokens.setSpotify_refresh_token(refreshToken);
        userTokens.setExpiresAt(Instant.now().plusSeconds(expiresIn));
        return userTokens;
    }
}
