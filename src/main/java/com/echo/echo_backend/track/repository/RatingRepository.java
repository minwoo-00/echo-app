package com.echo.echo_backend.track.repository;

import com.echo.echo_backend.track.entity.Rating;
import com.echo.echo_backend.track.entity.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {

    List<Rating> findByIdTrackId(String trackId);
    List<Rating> findByIdUserId(Long userId);
    Optional<Rating> findByIdUserIdAndIdTrackId(Long userId, String trackId);

    /*Rating save(Rating rating);
    List<Rating> findByTrackId(String trackId);
    List<Rating> findByUserId(Long userId);
    Optional<Rating> findByUserAndTrack(Long userId, String trackId);*/
}
