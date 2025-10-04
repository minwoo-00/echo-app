package com.echo.echo_backend.track.entity;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Track {
    private String track_id;
    private String title;
    private String artist;
    private String spotifyUri;
    private double avgRate;
    private int rateCnt;

    public Track(String track_id, String title, String artist, String spotifyUri) {
        this.track_id = track_id;
        this.title = title;
        this.artist = artist;
        this.spotifyUri = spotifyUri;
    }

    public void increaseRateCnt() {
        rateCnt++;
    }

    public void decreaseRateCnt() {
        if (rateCnt > 1) {
            rateCnt--;
        }
    }
}
