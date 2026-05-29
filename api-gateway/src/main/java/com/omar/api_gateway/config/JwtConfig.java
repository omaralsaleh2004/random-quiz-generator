package com.omar.api_gateway.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtConfig {

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {

        byte[] keyBytes = "da331418fab63650ff7189775f2224cc0bc1043ba509552ef12aaedd5c044ab8".getBytes();

        SecretKey secretKey =
                new SecretKeySpec(keyBytes, "HmacSHA256");

        return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
    }
}