package com.echo.echo_backend.user.repository;

import com.echo.echo_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findBySpotifyId(String spotifyId);
    List<User> findAll();
    /*User saveUser(User user);
    User findBySpotifyId(String spotify_id);
    User findById(Long id);
    User updateNickname(Long id, String nickname);
    User updateProfileImageUrl(Long id, String profileImageUrl);
    List<User> findAll();*/
}
