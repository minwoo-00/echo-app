package com.echo.echo_backend.track.repository;

import com.echo.echo_backend.track.entity.Rating;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RatingMemoryRepository implements RatingRepository{

    private static final Set<Rating> store = new HashSet<>();

    @Override
    public Rating save(Rating rating) {
        store.add(rating);
        return rating;
    }

    @Override
    public List<Rating> findByTrackId(String trackId) {
        return store.stream()
                .filter(rating -> Objects.equals(rating.getTrackId(), trackId))
                .toList();
    }

    @Override
    public List<Rating> findByUserId(Long userId) {
        return store.stream()
                .filter(rating -> Objects.equals(rating.getUserId(), userId))
                .toList();
    }

    @Override
    public Optional<Rating> findByUserAndTrack(Long userId, String trackId) {
        for (Rating rating : store) {
            if (rating.getUserId().equals(userId) && rating.getTrackId().equals(trackId)) {
                return Optional.of(rating);
            }
        }
        return Optional.empty();
    }
}
