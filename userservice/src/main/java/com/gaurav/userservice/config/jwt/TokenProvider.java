package com.gaurav.userservice.config.jwt;

import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;
import java.util.Map;

public interface TokenProvider {

    //get Signing Key
    SecretKey getSigningKey();

    //generate Token
    String generateToken(String username, Map<String, Object> claims);

    //validate Token
    boolean validateToken(String token);

    //getClaims from token
    Claims getClaimsFromToken(String token);

    //getUsername From Claims through token
    String getUsernameFromToken(String token);
}
