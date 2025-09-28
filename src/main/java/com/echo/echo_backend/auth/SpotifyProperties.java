package com.echo.echo_backend.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "echo.spotify")
public class SpotifyProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scopes;
    private String authorizeUrl;
    private String tokenUrl;
    private String userinfoUrl;
    // getters/setters
    // ...
}
