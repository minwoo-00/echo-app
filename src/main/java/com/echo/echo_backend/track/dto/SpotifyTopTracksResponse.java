package com.echo.echo_backend.track.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyTopTracksResponse {

    private List<TrackItem> items;

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TrackItem {
        private String id;
        private String name;
        private String uri;
        private List<Artist> artists;
        private Album album;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Artist {
        private String name;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Album {
        private String name;
        private List<Image> images;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Image {
        private String url;
        private int height;
        private int width;
    }

}
