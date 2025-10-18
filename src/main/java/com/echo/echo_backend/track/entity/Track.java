package com.echo.echo_backend.track.entity;

import com.echo.echo_backend.track.dto.ImageDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tracks")
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Track {

    @Id
    @Column(nullable = false, unique = true)
    private String trackId;

    @Column(nullable = false)
    private String title;

    @Column
    private String spotifyUri;

    @ElementCollection
    @CollectionTable(
            name = "track_artists",
            joinColumns = @JoinColumn(name = "track_id")
    )
    @Column(name = "artist_name")
    private List<String> artists;

    @ElementCollection
    @CollectionTable(
            name = "track_images",
            joinColumns = @JoinColumn(name = "track_id")
    )
    private List<Image> images;

    public Track(String track_id, String title, List<String> artist, String spotifyUri, List<Image> images) {
        this.trackId = track_id;
        this.title = title;
        this.artists = artist;
        this.spotifyUri = spotifyUri;
        this.images = images;
    }

    @Embeddable
    @NoArgsConstructor
    @Getter @Setter
    public static class Image{

        @Column(nullable = false)

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
