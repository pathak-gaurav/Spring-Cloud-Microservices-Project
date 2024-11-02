package com.gaurav.userservice.controller;

import com.gaurav.userservice.config.jwt.TokenProvider;
import com.gaurav.userservice.dao.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private TokenProvider tokenProvider;

    public LoginController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

        String generateToken = tokenProvider.generateToken(userDetails.getUsername(), Map.of("role", "USER"));

        return new ResponseEntity<>(generateToken, HttpStatus.CREATED);
    }
}
