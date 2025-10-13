package com.echo.echo_backend.track.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Builder
@Getter @Setter
public class Rating {
    private Long userId;
    private String trackId;
    private double rate;

    public Rating(Long userId, String trackId, double rate) {
        this.userId = userId;
        this.trackId = trackId;
        this.rate = rate;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Rating rating = (Rating) object;
        return Objects.equals(userId, rating.userId) && Objects.equals(trackId, rating.trackId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, trackId);
    }
}
