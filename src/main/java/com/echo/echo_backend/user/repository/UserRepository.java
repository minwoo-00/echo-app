package com.echo.echo_backend.user.repository;

import com.echo.echo_backend.user.entity.User;

import java.util.List;

public interface UserRepository {
    User saveUser(String spotify_id, String email);
    User findBySpotifyId(String spotify_id);
    User findById(Long id);
    User updateNickname(Long id, String nickname);
    User updateProfileImageUrl(Long id, String profileImageUrl);
    List<User> findAll();
}
