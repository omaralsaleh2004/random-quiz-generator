package com.omar.api_gateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
public class UserIdFilterConfig {

    @Bean
    public GlobalFilter roleFilter() {

        return (exchange, chain) -> {

            return exchange.getPrincipal()
                    .cast(JwtAuthenticationToken.class)
                    .flatMap(auth -> {

                        Jwt jwt = auth.getToken();

                        String userId = jwt.getClaim("userId").toString();
                        String role = jwt.getClaim("role");

                        // 🚨 BLOCK NON-ADMINS for quiz creation endpoint
                        if (exchange.getRequest().getPath().toString().startsWith("/admin")) {

                            if (!"ADMIN".equals(role)) {
                                exchange.getResponse().setStatusCode(
                                        org.springframework.http.HttpStatus.FORBIDDEN
                                );
                                return exchange.getResponse().setComplete();
                            }
                        }

                        ServerHttpRequest request = exchange.getRequest()
                                .mutate()
                                .header("X-User-Id", userId)
                                .header("X-User-Role", role)
                                .build();

                        return chain.filter(
                                exchange.mutate().request(request).build()
                        );
                    })
                    .switchIfEmpty(chain.filter(exchange));
        };
    }
}