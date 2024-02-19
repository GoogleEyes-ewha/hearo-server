package com.gdsc.hearo.global.security;

import com.gdsc.hearo.global.common.BaseResponseStatus;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {


    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.tokenExpirationTime}")
    private Long ACCESS_TOKEN_EXP_TIME;

    @Value("${spring.jwt.refreshTokenExpTime}")
    private Long REFRESH_TOKEN_EXP_TIME;


    // Access Token 생성
    public String createAccessToken(String loginId) {
        return createToken(loginId, ACCESS_TOKEN_EXP_TIME);
    }

    // refresh Token 생성
    public String createRefreshToken(String loginId) {
        return createToken(loginId, REFRESH_TOKEN_EXP_TIME);
    }

    // JWT 생성
    public String createToken(String loginId, long expTime) {
        Claims claims = Jwts.claims().setSubject(loginId);


        Date now = new Date();
        Date tokenValidity = new Date(now.getTime() + expTime); // 만료 시간

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    // JWT Claims 추출
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // 토큰에서 loginId 추출
    public String getLoginId(String token) {
        return parseClaims(token).getSubject();
    }


    // JWT 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new JwtException(BaseResponseStatus.INVALID_TOKEN.getMessage());
        } catch (ExpiredJwtException e) {
            throw new JwtException(BaseResponseStatus.EXPIRED_TOKEN.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new JwtException(BaseResponseStatus.UNSUPPORTED_TOKEN.getMessage());
        } catch (IllegalArgumentException e) {
            throw new JwtException(BaseResponseStatus.ACCESS_DENIED.getMessage());
        }
    }

    // Access Token 유효성 + 만료일자 확인
    public boolean validAccessToken(String accessToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // Refresh Token의 유효성 + 만료일자 확인
    public boolean validRefreshToken(String refreshToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
