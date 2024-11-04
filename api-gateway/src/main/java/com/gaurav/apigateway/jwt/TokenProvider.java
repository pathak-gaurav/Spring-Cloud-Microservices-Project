package com.gaurav.apigateway.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class TokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    //get signing Key
    public SecretKey getSingingKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    //Validate Token
    public boolean validateToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        try {
            Jwts.parser()
                    .verifyWith(getSingingKey())
                    .build()
                    .parseSignedClaims(token);
            return true;

        } catch (ExpiredJwtException exception) {
            return false;
        } catch (JwtException jwtException) {
            return false;
        }
    }

    //get Claims
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSingingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //get Username
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    //get UsernamePasswordAuthentication
    public UsernamePasswordAuthenticationToken authentication(String token) {
        Claims claims = getClaimsFromToken(token);
        List<String> roles = claims.get("roles", List.class);

        List<GrantedAuthority> authorities;
        if (roles != null) {
            authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList());
        } else {
            authorities = Collections.emptyList();
        }

        return new UsernamePasswordAuthenticationToken(
                getUsernameFromToken(token),
                null,
                authorities
        );
    }
}
