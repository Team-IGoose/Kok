package com.example.goose.common.service;

import com.example.goose.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;


    // Refresh Token 저장
    public void storeRefreshToken(String refreshToken, long expirationTimeMillis) {
        redisTemplate.opsForValue().set(jwtTokenProvider.getUserIdFromToken(refreshToken), refreshToken, expirationTimeMillis, TimeUnit.MILLISECONDS);
    }

    // Refresh Token 가져오기
    public String getRefreshToken(String uuid) {
        return redisTemplate.opsForValue().get(uuid);
    }

    public boolean isValidRefreshToken(String refreshToken) {
        String storedRefreshToken = redisTemplate.opsForValue().get(jwtTokenProvider.getUserIdFromToken(refreshToken));
        return storedRefreshToken != null && refreshToken.equals(storedRefreshToken);
    }

    // Refresh Token 삭제 (RTR 기법)
    public void deleteRefreshToken(String uuid) {
        redisTemplate.delete(uuid);
    }
}
