package com.echo.echo_backend;

import com.echo.echo_backend.auth.jwt.JwtAuthenticationFilter;
import com.echo.echo_backend.auth.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*@Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    @Value("${jwt.access-token-minutes}")
    private long jwtMinutes;

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(jwtSecret, jwtIssuer, jwtMinutes);
    }*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtProvider jwtProvider) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(reg -> reg
                        .requestMatchers(
                                "/auth/spotify/url",
                                "/oauth2/callback/spotify",
                                "/actuator/health",
                                "/users/*",
                                "/uploads/*"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                //.httpBasic(Customizer.withDefaults());
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtProvider),
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
