package com.echo.echo_backend.auth;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OAuthStateStore {

    private final StringRedisTemplate redis;

    public OAuthStateStore(StringRedisTemplate redis) {
        this.redis = redis;
    }

    // state → code_verifier 저장 (5분 TTL)
    public void save(String state, String codeVerifier) {
        String key = "oauth:state:" + state;
        redis.opsForValue().set(key, codeVerifier, 5, TimeUnit.MINUTES);
    }

    // state 검증 후 code_verifier 꺼내오기 (한 번 쓰면 삭제)
    public String consume(String state) {
        String key = "oauth:state:" + state;
        String codeVerifier = redis.opsForValue().get(key);
        if (codeVerifier != null) {
            redis.delete(key);
        }
        return codeVerifier;
    }
}
