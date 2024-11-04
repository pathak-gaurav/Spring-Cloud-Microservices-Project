package com.gaurav.apigateway.config;

import com.gaurav.apigateway.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers("/api/auth/**").permitAll()
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/fallback/**").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

}
