package com.echo.echo_backend.user.entity;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserTokens {

    private String spotify_id;
    private String spotify_access_token;
    private String spotify_refresh_token;
    private int expiresIn;

    public UserTokens(String spotify_id, String spotify_access_token, String spotify_refresh_token, int expiresIn) {
        this.spotify_id = spotify_id;
        this.spotify_access_token = spotify_access_token;
        this.spotify_refresh_token = spotify_refresh_token;
        this.expiresIn = expiresIn;
    }
}
