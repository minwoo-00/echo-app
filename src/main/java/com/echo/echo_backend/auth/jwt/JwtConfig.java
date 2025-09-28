package com.echo.echo_backend.auth.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    private final JwtProperties props;

    public JwtConfig(JwtProperties props) {
        this.props = props;
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(props.getSecret(), props.getIssuer(), props.getAccessTokenMinutes());
    }
}

