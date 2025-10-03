package com.echo.echo_backend.user.repository;

import com.echo.echo_backend.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserMemoryRepository implements UserRepository{

    private static final Map<String, User> storeUsers = new HashMap<>(); // ConcurrentHashMap
    private static long sequence = 0L;

    @Override
    public User saveUser(String spotify_id, String email) {
        User user = new User(spotify_id, email);
        user.setId(++sequence);
        storeUsers.put(user.getSpotify_id(), user);
        return user;
    }

    @Override
    public User updateNickname(Long id, String nickname) {
        User user = findById(id);
        user.setNickname(nickname);
        return user;
    }

    @Override
    public User updateProfileImageUrl(Long id, String profileImageUrl) {
        User user = findById(id);
        user.setProfileImageUrl(profileImageUrl);
        return user;
    }

    @Override
    public User findBySpotifyId(String spotify_id) {
        return storeUsers.get(spotify_id);
    }

    @Override
    public User findById(Long id) {
        Collection<User> values = storeUsers.values();
        for (User value : values) {
            if (value.getId().equals(id)) {
                return storeUsers.get(value.getSpotify_id());
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storeUsers.values());
    }
}
