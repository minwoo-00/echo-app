package com.echo.echo_backend.user.repository;

import com.echo.echo_backend.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;

public class UserMemoryRepository{

    private static final Map<String, User> storeUsers = new HashMap<>(); // ConcurrentHashMap
    private static long sequence = 0L;

    public User saveUser(User user) {
        user.setId(++sequence);
        storeUsers.put(user.getSpotifyId(), user);
        return user;
    }

    public User updateNickname(Long id, String nickname) {
        User user = findById(id);
        user.setNickname(nickname);
        return user;
    }

    public User updateProfileImageUrl(Long id, String profileImageUrl) {
        User user = findById(id);
        user.setProfileImageUrl(profileImageUrl);
        return user;
    }

    public User findBySpotifyId(String spotify_id) {
        return storeUsers.get(spotify_id);
    }

    public User findById(Long id) {
        Collection<User> values = storeUsers.values();
        for (User value : values) {
            if (value.getId().equals(id)) {
                return storeUsers.get(value.getSpotifyId());
            }
        }
        return null;
    }

    public List<User> findAll() {
        return new ArrayList<>(storeUsers.values());
    }
}
