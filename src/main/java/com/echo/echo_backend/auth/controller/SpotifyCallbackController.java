package com.echo.echo_backend.auth.controller;

import com.echo.echo_backend.auth.OAuthStateStore;
import com.echo.echo_backend.auth.SpotifyProperties;
import com.echo.echo_backend.auth.dto.SpotifyOAuthCallbackResponse;
import com.echo.echo_backend.auth.dto.SpotifyTokenResponse;
import com.echo.echo_backend.auth.jwt.JwtProvider;
import com.echo.echo_backend.user.service.UserService;
import com.echo.echo_backend.user.entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@RestController
public class SpotifyCallbackController {

    private final SpotifyProperties props;
    private final JwtProvider jwtProvider;
    private final OAuthStateStore stateStore;
    private final UserService userService;
    private final ObjectMapper mapper = new ObjectMapper();

    public SpotifyCallbackController(SpotifyProperties props, JwtProvider jwtProvider, OAuthStateStore stateStore, UserService userService) {
        this.props = props;
        this.stateStore = stateStore;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    @Operation(summary = "Spotify OAuth2 Callback", description = """
        Spotify 인증 이후 Redirect URI로 호출되는 콜백 엔드포인트입니다.
        Spotify로부터 전달받은 인증 코드를 기반으로 유저 정보를 조회하고,  
        Echo 서비스용 JWT 토큰(`echo_jwt`)을 발급합니다.  
        또한, 신규 사용자 여부(`isNewUser`)를 함께 반환합니다.
        
        ⚙️ 일반적으로 프론트엔드에서는 이 API 응답을 통해:
        - 신규 유저면 회원가입 절차를 진행하고
        - 기존 유저면 자동 로그인 처리를 수행합니다.
        """)
    @GetMapping("/oauth2/callback/spotify")
    public SpotifyOAuthCallbackResponse callback(
            @RequestParam String code,
            @RequestParam String state
    ) throws Exception {
        // 1. state 검증
        log.info("state 검증 시작");
        String codeVerifier = stateStore.consume(state);
        if (codeVerifier == null) {
            throw new IllegalStateException("Invalid state parameter");
        }
        log.info("state 검증 성공!");

        // 2. 토큰 교환 요청
        String body = "grant_type=authorization_code" +
                "&code=" + enc(code) +
                "&redirect_uri=" + enc(props.getRedirectUri()) +
                "&client_id=" + enc(props.getClientId()) +
                "&code_verifier=" + enc(codeVerifier) +
                "&client_secret=" + enc(props.getClientSecret());

        HttpRequest tokenReq = HttpRequest.newBuilder()
                .uri(URI.create(props.getTokenUrl()))
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> res = client.send(tokenReq, HttpResponse.BodyHandlers.ofString());

        if (res.statusCode() != 200) {
            throw new RuntimeException("Spotify token exchange failed: " + res.body());
        }

        SpotifyTokenResponse tokenResponse = mapper.readValue(res.body(), SpotifyTokenResponse.class);

        // 3. /me 호출해서 유저 정보 가져오기
        HttpRequest meReq = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/me"))
                .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                .GET()
                .build();

        HttpResponse<String> meRes = client.send(meReq, HttpResponse.BodyHandlers.ofString());
        if (meRes.statusCode() != 200) {
            throw new RuntimeException("Spotify /me failed: " + meRes.body());
        }

        JsonNode meJson = mapper.readTree(meRes.body());
        String spotifyId = meJson.get("id").asText();
        String email = meJson.has("email") ? meJson.get("email").asText() : null;

        log.info("spotifyId={}, email={}", spotifyId, email);
        // 4. DB 저장 (신규/기존 판별)
        boolean isNewUser = false;
        User user = userService.findBySpotifyId(spotifyId);
        if (user == null) {
            // 신규 유저 → DB insert
            user = userService.createUser(spotifyId, email,
                    tokenResponse.getAccessToken(),
                    tokenResponse.getRefreshToken(),
                    tokenResponse.getExpiresIn());
            isNewUser = true;
        } else {
            // 기존 유저 → 토큰 갱신
            userService.updateTokens(user.getId(),
                    tokenResponse.getAccessToken(),
                    tokenResponse.getRefreshToken(),
                    tokenResponse.getExpiresIn());
        }

        // 5. JWT 발급 (예: Spotify access token을 DB에 저장하고 userId 기준 JWT 발급)
        // 여기선 access_token을 userId 대용으로 넣음 (실제는 Spotify /me API로 id 받아오기)
        String jwt = jwtProvider.generateToken(user.getId().toString());

        // 6. 반환 (프론트로 JWT 전달)
        return new SpotifyOAuthCallbackResponse(jwt, isNewUser);
    }

    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}

