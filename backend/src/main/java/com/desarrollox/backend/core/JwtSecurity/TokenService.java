package com.desarrollox.backend.core.JwtSecurity;

import org.springframework.stereotype.Service;
import com.desarrollox.backend.api_register.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService{

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    @Override
    public Claims extractAllClaims(String token) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        // Parsear Token
        Jws<Claims> jws = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token);

        // Retornar el cuerpo (Payload) del token
        return jws.getBody();
    }

    @Override
    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("name", user.getName());
        claims.put("birthdate", user.getBirthdate().toString());
        claims.put("role", user.getRole());
        claims.put("type", "access");

        return createToken(claims, user.getEmail(), jwtExpiration);
    }

    @Override
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("type", "refresh");
        claims.put("jti", UUID.randomUUID().toString());
        return createToken(claims, user.getEmail(), refreshExpiration);
    }

    @Override
    public Long getTokenExpiration() {
        return jwtExpiration / 1000;
    }

    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(key)
            .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
}