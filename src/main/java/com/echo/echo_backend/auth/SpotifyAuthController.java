package com.echo.echo_backend.auth;

import com.echo.echo_backend.auth.SpotifyProperties;
import com.echo.echo_backend.auth.PkceUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
public class SpotifyAuthController {

    private final SpotifyProperties props;
    private final OAuthStateStore stateStore;

    public SpotifyAuthController(SpotifyProperties props, OAuthStateStore stateStore) {
        this.props = props;
        this.stateStore = stateStore;
    }

    @Operation(
            summary = "Get Spotify authorization URL",
            description = "Generates the Spotify OAuth2 authorization URL with PKCE. "
                    + "The server creates a state, code_verifier, and code_challenge, "
                    + "stores them for later token exchange, and returns the authorization URL. "
                    + "The frontend should redirect the user to this URL to start the Spotify login flow."
    )
    @GetMapping("/auth/spotify/url")
    public Map<String, String> getAuthorizationUrl(HttpSession session) {
        String state = PkceUtil.randomUrlSafeString(32);
        String codeVerifier = PkceUtil.randomUrlSafeString(64);
        String codeChallenge = PkceUtil.sha256Base64Url(codeVerifier);

        stateStore.save(state, codeVerifier);

        String url = props.getAuthorizeUrl()
                + "?response_type=code"
                + "&client_id=" + enc(props.getClientId())
                + "&redirect_uri=" + enc(props.getRedirectUri())
                + "&scope=" + enc(props.getScopes())
                + "&state=" + enc(state)
                + "&code_challenge=" + enc(codeChallenge)
                + "&code_challenge_method=S256";

        return Map.of("authorizationUrl", url);
    }

    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}
