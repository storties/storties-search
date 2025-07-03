package org.example.stortiessearch.global.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtProperties {

    @Value("${jwt.secret}")
    public String SECRET;

    @Value("${jwt.access-token-expiration}")
    public long ACCESS_TOKEN_EXPIRES_AT;

    @Value("${jwt.refresh-token-expiration}")
    public long REFRESH_TOKEN_EXPIRES_AT;

    static public final String ROLE = "role";

    static public final String ID = "id";

    static public final String TOKEN_TYPE = "tokenType";

    static public final String TOKEN = "token";

    static public final String EXPIRES_IN = "expiresIn";

    static public final String EXPIRES_AT = "expiresAt";

    static public final String ACCESS_TOKEN = "ACCESS_TOKEN";

    static public final String REFRESH_TOKEN = "REFRESH_TOKEN";
}
