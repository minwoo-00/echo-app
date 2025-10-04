package com.echo.echo_backend.track.controller;

import com.echo.echo_backend.auth.service.SpotifyAuthService;
import com.echo.echo_backend.track.dto.TrackDto;
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

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
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
}
