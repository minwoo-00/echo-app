package com.echo.echo_backend.track.repository;

import com.echo.echo_backend.track.entity.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingRepository {
    Rating save(Rating rating);
    List<Rating> findByTrackId(String trackId);
    List<Rating> findByUserId(Long userId);
    Optional<Rating> findByUserAndTrack(Long userId, String trackId);
}
