package org.example.stortiessearch.global.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.example.stortiessearch.global.exception.error.ErrorCodes;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.List;

@Slf4j
@Component
public class JwtParser {

    private final io.jsonwebtoken.JwtParser jwtParser;

    public JwtParser(JwtProperties jwtProperties) {
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.SECRET.getBytes());
        this.jwtParser = Jwts.parserBuilder()
            .setSigningKey(key)
            .setAllowedClockSkewSeconds(60)
            .build();
    }

    /**
     * 엑세스 토큰에서 인증 정보 추출 (ROLE_ 접두사 포함)
     */
    public Authentication getAuthentication(String accessToken) {
        String role = getRoleByAccessToken(accessToken);
        return new UsernamePasswordAuthenticationToken(
            getId(accessToken),
            null,
            List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }

    public String getRoleByAccessToken(String accessToken) {
        return getClaims(accessToken).get(JwtProperties.ROLE, String.class);
    }

    public Long getId(String accessToken) {
        return Long.valueOf(getClaims(accessToken).getSubject());
    }

    /**
     * 엑세스 토큰 유효성 검사 (만료 및 토큰 타입)
     * 만료 시 TokenExpiredException 발생
     */
    public void validateAccessToken(String accessToken) {
        Claims claims = getClaims(accessToken);

        if (!JwtProperties.ACCESS_TOKEN.equals(
            claims.get(JwtProperties.TOKEN_TYPE, String.class))) {
            throw ErrorCodes.INVALID_TOKEN.throwException();
        }
    }

    /**
     * 토큰에서 Claims 추출 (서명 검증 포함)
     */
    private Claims getClaims(String token) {
        try {
            return jwtParser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw ErrorCodes.TOKEN_EXPIRED.throwException();
        } catch (Exception e) {
            throw ErrorCodes.INVALID_TOKEN.throwException();
        }

    }
}
