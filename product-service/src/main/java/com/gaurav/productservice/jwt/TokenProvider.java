package com.gaurav.productservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collections;

@Configuration
public class TokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    //validate Token
    public boolean validateToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException exception) {
            return false;
        } catch (JwtException e) {
            return false;
        }
    }

    //getClaims
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //getUsername from Token
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    //getSigning Key
    public SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    //getAuthentication
    public UsernamePasswordAuthenticationToken usernamePasswordAuthentication(String token) {
        return new UsernamePasswordAuthenticationToken(getUsernameFromToken(token), null, Collections.emptyList());
    }
}
