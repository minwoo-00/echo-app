package com.echo.echo_backend.track.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class TrackDto {
    private String name;
    private String spotifyId;
    private String artist;
    private String spotifyUri;
    private List<ImageDto> images;

    public TrackDto(String name, String spotifyId, String artist, String spotifyUri, List<ImageDto> images) {
        this.name = name;
        this.spotifyId = spotifyId;
        this.artist = artist;
        this.spotifyUri = spotifyUri;
        this.images = images;
    }
}
