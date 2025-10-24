package com.echo.echo_backend.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SpotifyOAuthCallbackResponse {
    private String echo_jwt;
    private boolean isNewUser;

    public SpotifyOAuthCallbackResponse(String echo_jwt, boolean isNewUser) {
        this.echo_jwt = echo_jwt;
        this.isNewUser = isNewUser;
    }
}
