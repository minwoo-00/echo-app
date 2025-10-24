package com.echo.echo_backend.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthorizationUrlResponse {
    private String authorizationUrl;

    public AuthorizationUrlResponse(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }
}
