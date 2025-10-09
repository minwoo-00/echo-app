package com.echo.echo_backend.track.entity;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Rating {
    private Long userId;
    private String trackId;
    private double rate;
    private String review;

    public Rating(Long userId, String trackId, double rate, String review) {
        this.userId = userId;
        this.trackId = trackId;
        this.rate = rate;
        this.review = review;
    }

}
