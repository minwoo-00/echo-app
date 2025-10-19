package com.echo.echo_backend.track.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

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

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
            updatedAt = LocalDateTime.now();
        }
    }
}
