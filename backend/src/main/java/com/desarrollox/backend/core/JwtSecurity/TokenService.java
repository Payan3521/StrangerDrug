package com.desarrollox.backend.core.JwtSecurity;

import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService{

    @Value("${jwt.secret}")
    private String jwtSecret;

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
    
}