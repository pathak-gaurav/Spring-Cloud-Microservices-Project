package com.gaurav.userservice.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Date getDate(long expiration) {
        return new Date(new Date().getTime() + expiration);
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    public String generateToken(String username, Map<String, Object> claims) {
        Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(getDate(0))
                .expiration(getDate(expiration))
                .signWith(getSigningKey())
                .compact();
    }

}
