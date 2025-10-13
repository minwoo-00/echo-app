package com.echo.echo_backend.track.repository;

import com.echo.echo_backend.track.entity.Track;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class TrackMemoryRepository implements TrackRepository{

    private static final Map<String, Track> store = new HashMap<>();

    @Override
    public Track save(Track track) {
        store.put(track.getTrack_id(), track);
        return track;
    }

    @Override
    public Optional<Track> findById(String trackId) {
        return Optional.ofNullable(store.get(trackId));
    }

    @Override
    public boolean isContain(String trackId) {
        return store.containsKey(trackId);
    }
}
