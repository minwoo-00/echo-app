package com.echo.echo_backend.track.dto;

import com.echo.echo_backend.track.entity.Track;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ImageDto {
    private String url;
    private int height;
    private int width;

    public ImageDto(String url, int height, int width) {
        this.url = url;
        this.height = height;
        this.width = width;
    }

    public static List<ImageDto> toImageDto(List<Track.Image> images) {
        return images.stream().map(image -> {return new ImageDto(image.getUrl(), image.getHeight(), image.getWidth());})
                .toList();
    }
}
