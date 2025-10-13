package com.echo.echo_backend.track.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CommentRequest {
    private String name;
    private String spotifyId;
    private List<String> artist;
    private String spotifyUri;
    private List<ImageDto> images;
    private String content;
}
