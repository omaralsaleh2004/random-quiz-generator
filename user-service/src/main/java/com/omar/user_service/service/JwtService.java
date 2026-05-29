package com.omar.user_service.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final String SECRET = "da331418fab63650ff7189775f2224cc0bc1043ba509552ef12aaedd5c044ab8";

    public String generateToken(Integer userId,String email, String role) {
        return Jwts
                .builder()
                .subject(email)
                .claim("userId" , userId)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000L * 60 * 60 * 24 * 2
                        )
                )
                .signWith(Keys
                                .hmacShaKeyFor(SECRET.getBytes()),
                        Jwts.SIG.HS256
                )
                .compact();
    }
}
