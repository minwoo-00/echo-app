package com.echo.echo_backend.track.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "ratings")
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    @EmbeddedId
    private RatingId id;

    @Column(nullable = false)
    private double rate;

    public Rating(Long userId, String trackId, double rate) {
        this.id = new RatingId(userId, trackId);
        this.rate = rate;
    }

    public Long getUserId() {
        return id != null ? id.getUserId() : null;
    }

    public String getTrackId() {
        return id != null ? id.getTrackId() : null;
    }
}
