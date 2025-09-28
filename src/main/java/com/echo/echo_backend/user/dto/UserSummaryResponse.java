package com.echo.echo_backend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserSummaryResponse {

    private Long id;
    private String nickname;

    public UserSummaryResponse(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
