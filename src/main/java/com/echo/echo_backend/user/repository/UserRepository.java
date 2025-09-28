package com.echo.echo_backend.user.repository;

import com.echo.echo_backend.user.entity.User;

public interface UserRepository {
    User saveUser(User user);
    User findBySpotifyId(String spotify_id);
    User findById(Long id);
    User updateNickname(Long id, String nickname);
}
