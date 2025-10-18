package com.echo.echo_backend.user.repository;

import com.echo.echo_backend.user.entity.UserTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokensRepository extends JpaRepository<UserTokens, String> {

    Optional<UserTokens> findBySpotifyId(String spotifyId);

    /*UserTokens saveTokens(UserTokens userTokens);
    UserTokens updateTokens(String spotify_id, String accessToken, String refreshToken, int expiresIn);
    UserTokens findTokenBySpotifyId(String spotify_id);*/
}
