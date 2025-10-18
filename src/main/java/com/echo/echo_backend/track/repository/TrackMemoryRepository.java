package com.echo.echo_backend.track.repository;

import com.echo.echo_backend.track.entity.Track;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TrackMemoryRepository {

    private static final Map<String, Track> store = new HashMap<>();

    public Track save(Track track) {
        store.put(track.getTrackId(), track);
        return track;
    }

    public Optional<Track> findById(String trackId) {
        return Optional.ofNullable(store.get(trackId));
    }

    public boolean isContain(String trackId) {
        return store.containsKey(trackId);
    }
}
