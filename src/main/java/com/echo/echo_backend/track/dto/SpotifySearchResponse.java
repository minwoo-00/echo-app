package com.echo.echo_backend.track.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifySearchResponse {
    private Tracks tracks;

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tracks {
        private List<TrackItem> items;
    }

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TrackItem {
        private String name; // 곡 이름
        private String id; // 곡 spotify_id
        private String uri;
        private List<Artist> artists;
        private Album album;
    }

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Artist {
        private String name; // 아티스트 이름
    }

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Album {
        private List<Image> images;
    }

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Image {
        private String url;
        private int height;
        private int width;
    }

}
