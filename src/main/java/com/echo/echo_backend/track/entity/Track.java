package com.echo.echo_backend.track.entity;

import com.echo.echo_backend.track.dto.ImageDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter @Setter
public class Track {
    private String track_id;
    private String title;
    private List<String> artist;
    private String spotifyUri;
    private List<Image> images;

    public Track(String track_id, String title, List<String> artist, String spotifyUri, List<Image> images) {
        this.track_id = track_id;
        this.title = title;
        this.artist = artist;
        this.spotifyUri = spotifyUri;
        this.images = images;
    }

    @Getter @Setter
    public static class Image{
        private String url;
        private int height;
        private int width;

        public Image(String url, int height, int width) {
            this.url = url;
            this.height = height;
            this.width = width;
        }
    }

    public static List<Image> toImage(List<ImageDto> imageDto) {
        return imageDto.stream().map(image -> {return new Image(image.getUrl(), image.getHeight(), image.getWidth());})
                .toList();
    }

}
