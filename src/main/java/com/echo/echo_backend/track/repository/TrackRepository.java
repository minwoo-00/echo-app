package com.echo.echo_backend.track.repository;

import com.echo.echo_backend.track.entity.Track;

import java.util.Optional;

public interface TrackRepository {
    Track save(Track track);
    Optional<Track> findById(String trackId);
    boolean isContain(String trackId);
}
