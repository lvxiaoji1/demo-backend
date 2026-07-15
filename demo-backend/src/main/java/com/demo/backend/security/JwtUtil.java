package com.demo.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    // 在实际项目中，这个密钥应该放在环境变量或配置文件中，不要写死在代码里
    private static final String SECRET = "Y29kZXgtZGVtby1iYWNrZW5kLWp3dC1zZWNyZXQta2V5LTIwMjY=";
    private static final long EXPIRATION = 86400000; // 24小时

    private final SecretKey key;

    public JwtUtil() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Long userId, String email) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}