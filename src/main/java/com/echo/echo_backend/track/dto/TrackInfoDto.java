package com.echo.echo_backend.track.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter @Setter
public class TrackInfoDto {

    private String name;
    private String spotifyId;
    private List<String> artist;
    private String spotifyUri;
    private List<ImageDto> images;

    private Double avgRate;
    private int rateCnt;
    private Double myRate;
    private List<CommentDto> comments;

/*
    public TrackInfoDto(TrackDto trackDto) {
        this.name = trackDto.getName();
        this.spotifyId = trackDto.getSpotifyId();
        this.artist = trackDto.getArtist();
        this.spotifyUri = trackDto.getSpotifyUri();
        this.images = trackDto.getImages();
    }
*/

    public void fromTrackDto(TrackDto trackDto) {
        this.name = trackDto.getName();
        this.spotifyId = trackDto.getSpotifyId();
        this.artist = trackDto.getArtist();
        this.spotifyUri = trackDto.getSpotifyUri();
        this.images = trackDto.getImages();    }
}
