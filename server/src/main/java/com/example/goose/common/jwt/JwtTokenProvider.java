package com.example.goose.common.jwt;

import io.jsonwebtoken.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenValidity;

    @Getter
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenValidity;

    @Value("${jwt.email-token-expiration}")
    private long emailTokenValidity;






    // Access Token 생성
    public String generateAccessToken(String uuid) {
        return Jwts.builder()
                .setSubject(uuid) // Setting user id as the subject
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // Refresh Token 생성
    public String generateRefreshToken(String uuid) {
        return Jwts.builder()
                .setSubject(uuid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String refreshAccessToken(String refreshToken) throws Exception {
        return Jwts.builder()
                .setSubject(getUserIdFromToken(refreshToken)) // Setting user id as the subject
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }



    /**
     * 토큰 결합 후 암호화
     * @param accessToken
     * @param refreshToken
     * @return
     * @throws Exception
     */
    public String generateCombinedToken(String accessToken, String refreshToken) throws Exception {
        String combinedToken = accessToken + "&" + refreshToken;
        return TokenEncryptor.encrypt(combinedToken);
    }

    /**
     * 클라이언트에서 전달 받은 토큰 디코딩
     * @param encryptedToken
     * @return
     * @throws Exception
     */
    public String [] parseCombinedToken(String encryptedToken) throws Exception {
        String decryptedToken = TokenEncryptor.decrypt(encryptedToken);
        return decryptedToken.split("&");
    }



    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            // Log token expired event
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }



    // 토큰에서 사용자 정보 추출
    public String getUserIdFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            // Token is expired, handle this scenario
            return true;
        }
    }


}
