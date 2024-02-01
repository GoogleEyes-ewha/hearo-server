package com.gdsc.hearo.global.security;

import com.gdsc.hearo.domain.member.entity.Member;
import com.gdsc.hearo.domain.member.repository.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private final Key key;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;
    private final MemberRepository memberRepository;

    public JwtUtil(
            @Value("${spring.jwt.secret}") String secretKey, @Value("${spring.jwt.tokenExpirationTime}") long accessTokenExpTime, @Value("${spring.jwt.refreshTokenExpTime}") long refreshTokenExpTime, MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime; // 2시간
        this.refreshTokenExpTime = refreshTokenExpTime; // 1주일
    }

    // Access Token 생성
    public String createAccessToken(String loginId) {
        return createToken(loginId, accessTokenExpTime);
    }

    // refresh Token 생성
    public String createRefreshToken(String loginId) {
        return createToken(loginId, refreshTokenExpTime);
    }

    // JWT 생성
    public String createToken(String loginId, long expTime) {
        Claims claims = Jwts.claims().setSubject(loginId);

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    // JWT Claims 추출
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // Token에서 Member 객체 생성
    public Member getMember(String token) {
        String loginId = parseClaims(token).getSubject();
        Member member = memberRepository.findByLoginId(loginId);

        return member;
    }

    // JWT 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }
}
