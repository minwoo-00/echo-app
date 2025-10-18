package com.echo.echo_backend.auth.service;

import com.echo.echo_backend.user.entity.UserTokens;
import com.echo.echo_backend.user.repository.UserTokensRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

@Service
public class SpotifyAuthService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserTokensRepository userTokensRepository;

    @Value("${echo.spotify.client-id}")
    private String clientId;
    @Value("${echo.spotify.client-secret}")
    private String clientSecret;
    private final String tokenUrl = "https://accounts.spotify.com/api/token";


    public SpotifyAuthService(UserTokensRepository userTokensRepository) {
        this.userTokensRepository = userTokensRepository;
    }

    // 갱신된 accessToken 반환
    @Transactional
    public String getValidAccessToken(String spotify_id) {
        UserTokens token = userTokensRepository.findBySpotifyId(spotify_id) //수정 필요
               .orElseThrow(() -> new RuntimeException("Spotify token not found"));

        // 유효성 체크 (30초 버퍼)
        if (Instant.now().isAfter(token.getExpiresAt().minusSeconds(30))) {
            refreshAccessToken(token);
        }

        return token.getSpotifyAccessToken();
    }

    // 토큰 갱신
    @Transactional
    private void refreshAccessToken(UserTokens token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", token.getSpotifyRefreshToken());
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

        Map<String, Object> responseBody = response.getBody();

        if (responseBody != null && responseBody.containsKey("access_token")) {
            String newAccessToken = (String) responseBody.get("access_token");
            String newRefreshToken = (String) responseBody.get("refresh_token");
            Integer expiresIn = (Integer) responseBody.get("expires_in");

            // 엔티티 갱신
            token.updateTokens(newAccessToken, newRefreshToken, expiresIn);
            //userTokensRepository.save(token);
        } else {
            throw new RuntimeException("Failed to refresh Spotify access token");
        }
    }
}
