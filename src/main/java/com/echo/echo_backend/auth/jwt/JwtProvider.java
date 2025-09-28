package com.echo.echo_backend.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.oauth2.jwt.JwtException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtProvider {
    private final SecretKey secretKey;
    private final String issuer;
    private final long validityMinutes;

    public JwtProvider(String secret, String issuer, long validityMinutes) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.validityMinutes = validityMinutes * 60 * 1000;
    }

    public String generateToken(String userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityMinutes);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setIssuer(issuer)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(String userId, Map<String, Object> extraClaims) { //추가 클레임 있을 때
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityMinutes);

        JwtBuilder builder = Jwts.builder()
                .setSubject(userId)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256);

        if (extraClaims != null && !extraClaims.isEmpty()) {
            builder.addClaims(extraClaims);
        }

        return builder.compact();
    }

    public Claims validate(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .requireIssuer(issuer)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

