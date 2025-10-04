package com.echo.echo_backend.track.dto;

import lombok.Getter;
import lombok.Setter;

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
}
