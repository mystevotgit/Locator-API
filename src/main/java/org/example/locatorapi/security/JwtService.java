package org.example.locatorapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationMillis = 1000 * 60 * 60 * 24; // 24 hour only for testing, remove 24 in prod

    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
            .signWith(secretKey)
            .compact();
    }

    public String extractUsername(String token) {
        return parseToken(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token);
    }
}