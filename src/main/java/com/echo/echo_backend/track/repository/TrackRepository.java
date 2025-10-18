package com.echo.echo_backend.track.repository;

import com.echo.echo_backend.track.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<Track, String> {

    boolean existsByTrackId(String trackId);
    /*Track save(Track track);
    Optional<Track> findById(String trackId);
    boolean isContain(String trackId);*/
}
