package com.echo.echo_backend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NicknameCheckResponse {
    private String isPossible;

    public NicknameCheckResponse(String isPossible) {
        this.isPossible = isPossible;
    }
}
