package com.echo.echo_backend.track.service;

import com.echo.echo_backend.auth.service.SpotifyAuthService;
import com.echo.echo_backend.track.dto.*;
import com.echo.echo_backend.track.entity.Rating;
import com.echo.echo_backend.track.repository.CommentRepository;
import com.echo.echo_backend.track.repository.RatingRepository;
import com.echo.echo_backend.track.repository.TrackRepository;
import com.echo.echo_backend.user.entity.User;
import com.echo.echo_backend.user.repository.UserRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TrackService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://api.spotify.com/v1";
    private final UserRepository userRepository;
    private final SpotifyAuthService spotifyAuthService;
    private final TrackRepository trackRepository;
    private final RatingRepository ratingRepository;
    private final CommentRepository commentRepository;

    public TrackService(UserRepository userRepository, SpotifyAuthService spotifyAuthService, TrackRepository trackRepository, RatingRepository ratingRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.spotifyAuthService = spotifyAuthService;
        this.trackRepository = trackRepository;
        this.ratingRepository = ratingRepository;
        this.commentRepository = commentRepository;
    }

    public List<TrackDto> searchTracks(Long userId, String query) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        String accessToken = spotifyAuthService.getValidAccessToken(user.getSpotifyId());

        //String url = BASE_URL + "/search?q=" + UriUtils.encode(query, StandardCharsets.UTF_8)
        //        + "&type=track&limit=20";
        String url = BASE_URL + "/search?q=" + query
                  + "&type=track&market=KR&limit=20";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<SpotifySearchResponse> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, SpotifySearchResponse.class);

        List<SpotifySearchResponse.TrackItem> items = response.getBody().getTracks().getItems();

        return items.stream().map(item -> {
            List<String> artistName = item.getArtists().stream().map(SpotifySearchResponse.Artist::getName).toList();

            List<ImageDto> images = (item.getAlbum() != null) ? (item.getAlbum().getImages().stream()
                    .map(img -> new ImageDto(img.getUrl(), img.getHeight(), img.getWidth()))
                    .toList())
                    : List.of();

            return new TrackDto(
                    item.getName(),
                    item.getId(),
                    artistName,
                    item.getUri(),
                    images
            );
        }).toList();
    }


    public List<TrackDto> getUserTopTracks(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        String accessToken = spotifyAuthService.getValidAccessToken(user.getSpotifyId());

        String url = BASE_URL + "/me/top/tracks" + "?limit=20";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<SpotifyTopTracksResponse> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, SpotifyTopTracksResponse.class);

        List<SpotifyTopTracksResponse.TrackItem> items = response.getBody().getItems();

        return items.stream().map(item -> {
            List<String> artistName = item.getArtists().stream().map(SpotifyTopTracksResponse.Artist::getName).toList();

            List<ImageDto> images = (item.getAlbum() != null) ? (item.getAlbum().getImages().stream()
                    .map(img -> new ImageDto(img.getUrl(), img.getHeight(), img.getWidth()))
                    .toList())
                    : List.of();

            return new TrackDto(
                    item.getName(),
                    item.getId(),
                    artistName,
                    item.getUri(),
                    images
            );
        }).toList();
    }

    public ReviewDto getTrackReview(String trackId, Long userId) { // rating + comment 반환

        List<Rating> ratings = ratingRepository.findByIdTrackId(trackId);
        int rateCnt = ratings.size();
        Double avgRate = ratings.stream().mapToDouble(Rating::getRate).average().orElse(0.0);
        Double myRate = ratings.stream()
                .filter(r -> r.getUserId().equals(userId))
                .map(Rating::getRate)
                .findFirst()
                .orElse(0.0);

        List<CommentDto> comments = commentRepository.findByTrackId(trackId)
                .stream()
                .map(CommentDto::fromEntity)
                .toList();

        return ReviewDto.builder()
                .spotifyId(trackId)
                .avgRate(avgRate)
                .rateCnt(rateCnt)
                .myRate(myRate)
                .comments(comments)
                .build();
    }

    public TrackInfoDto getTrackInfo(TrackDto trackDto, Long userId) {  //trackDto + rating + comment 반환

        List<Rating> ratings = ratingRepository.findByIdTrackId(trackDto.getSpotifyId());
        int rateCnt = ratings.size();
        Double avgRate = ratings.stream().mapToDouble(Rating::getRate).average().orElse(0.0);
        Double myRate = ratings.stream()
                .filter(r -> r.getUserId().equals(userId))
                .map(Rating::getRate)
                .findFirst()
                .orElse(0.0);

        List<CommentDto> comments = commentRepository.findByTrackId(trackDto.getSpotifyId())
                .stream()
                .map(CommentDto::fromEntity)
                .toList();

        return TrackInfoDto.builder()
                .name(trackDto.getName())
                .spotifyId(trackDto.getSpotifyId())
                .artist(trackDto.getArtist())
                .spotifyUri(trackDto.getSpotifyUri())
                .images(trackDto.getImages())
                .avgRate(avgRate)
                .rateCnt(rateCnt)
                .myRate(myRate)
                .comments(comments)
                .build();

    }

}
