package com.echo.echo_backend.track.controller;

import com.echo.echo_backend.auth.service.SpotifyAuthService;
import com.echo.echo_backend.track.dto.*;
import com.echo.echo_backend.track.repository.CommentRepository;
import com.echo.echo_backend.track.repository.RatingRepository;
import com.echo.echo_backend.track.service.CommentService;
import com.echo.echo_backend.track.service.RatingService;
import com.echo.echo_backend.track.service.TrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/track")
public class TrackController {

    private final TrackService trackService;
    private final RatingService ratingService;
    private final CommentService commentService;

    public TrackController(TrackService trackService, RatingService ratingService, CommentService commentService) {
        this.trackService = trackService;
        this.ratingService = ratingService;
        this.commentService = commentService;
    }

    @Operation(summary = "Search track by query", description = "Get search results on spotify as a search keyword.")
    @GetMapping("/search")
    public ResponseEntity<List<TrackDto>> searchTracks(
            @Parameter(description = "The search keyword") @RequestParam String query,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(trackService.searchTracks(userId, query));
    }

    @Operation(summary = "Onboarding", description = "Get the current user's top tracks based on calculated affinity.")
    @GetMapping("/onboarding")
    public ResponseEntity<List<TrackDto>> onboarding(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(trackService.getUserTopTracks(userId));
    }

    @Operation(summary = "Get track's review", description = "Get the track information including myRate, avgRate and comments.")
    @GetMapping("/{trackId}/reviews")
    public ResponseEntity<ReviewDto> getTrackReview(@PathVariable String trackId, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(trackService.getTrackReview(trackId, userId));
    }
    //GET 메소드 사용하면 requestbody 사용 불가 -> track_Id만 넘겨받고 해당 트랙의 평점, 평가수, 나의 점수, 코멘트 정보만 리턴

    @Operation(summary = "Register or update rating", description = "User rates a track (creates Track if not exists).")
    @PostMapping("/ratings")
    public ResponseEntity<TrackInfoDto> createRating(
            @RequestBody RatingRequest request,
            Authentication authentication)
    {
        Long userId = (Long) authentication.getPrincipal();
        TrackInfoDto response = ratingService.createOrUpdateRating(request, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create comment", description = "User leaves a comment on a track (creates Track if not exists).")
    @PostMapping("/comments")
    public ResponseEntity<TrackInfoDto> createComment(
            @RequestBody CommentRequest request,
            Authentication authentication)
    {
        Long userId = (Long) authentication.getPrincipal();
        TrackInfoDto response = commentService.createOrUpdateComment(request, userId);
        return ResponseEntity.ok(response);
    }
}
