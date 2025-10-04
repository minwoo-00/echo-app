package com.echo.echo_backend.track.service;

import com.echo.echo_backend.auth.service.SpotifyAuthService;
import com.echo.echo_backend.track.dto.ImageDto;
import com.echo.echo_backend.track.dto.SpotifySearchResponse;
import com.echo.echo_backend.track.dto.TrackDto;
import com.echo.echo_backend.user.entity.User;
import com.echo.echo_backend.user.repository.UserRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class TrackService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://api.spotify.com/v1";
    private final UserRepository userRepository;
    private final SpotifyAuthService spotifyAuthService;

    public TrackService(UserRepository userRepository, SpotifyAuthService spotifyAuthService) {
        this.userRepository = userRepository;
        this.spotifyAuthService = spotifyAuthService;
    }

    public List<TrackDto> searchTracks(Long userId, String query) {
        User user = userRepository.findById(userId);
        String accessToken = spotifyAuthService.getValidAccessToken(user.getSpotify_id());

        //String url = BASE_URL + "/search?q=" + UriUtils.encode(query, StandardCharsets.UTF_8)
        //        + "&type=track&limit=20";
        String url = BASE_URL + "/search?q=" + query
                  + "&type=track&limit=20";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<SpotifySearchResponse> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, SpotifySearchResponse.class);

        List<SpotifySearchResponse.TrackItem> items = response.getBody().getTracks().getItems();

        return items.stream().map(item -> {
            String artistName = item.getArtists().isEmpty() ? "" : item.getArtists().get(0).getName();

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

}
