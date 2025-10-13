package com.echo.echo_backend.track.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter @Setter
public class ReviewDto {
    private String spotifyId; //track_Id
    private Double avgRate;
    private int rateCnt;
    private Double myRate;
    private List<CommentDto> comments;
}
