package org.example.stortiessearch.global.authentication;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.example.stortiessearch.global.exception.error.ErrorCodes;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenParser {

    private final JwtProperties jwtProperties;

    private final JwtParser jwtParser;

    public JwtTokenParser(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.SECRET.getBytes());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    /**
     * 권한 가져오기
     * @param accessToken 엑세스 토큰
     * @return 권한
     */
    public Authentication getAuthentication(String accessToken) {
        Long id = getId(accessToken);
        String role = getRoleByAccessToken(accessToken);
        return new UsernamePasswordAuthenticationToken(id, null,
            List.of(new SimpleGrantedAuthority(role)));
    }

    public String getRoleByAccessToken(String accessToken) {
        return jwtParser.parseClaimsJws(accessToken).getBody().get(JwtProperties.ROLE, String.class);
    }

    public Long getId(String accessToken) {
        return jwtParser.parseClaimsJws(accessToken).getBody().get(JwtProperties.ID, Long.class);
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtProperties.SECRET.getBytes());

            io.jsonwebtoken.JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build();

            Date expiration = parser.parseClaimsJws(accessToken).getBody().getExpiration();
            boolean isExpired = expiration.before(new Date());

            if (isExpired) throw ErrorCodes.TOKEN_EXPIRED.throwException();

            String tokenType = parser.parseClaimsJws(accessToken).getBody().get(JwtProperties.TOKEN_TYPE, String.class);

            return tokenType.equals(JwtProperties.ACCESS_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
