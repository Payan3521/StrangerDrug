package com.desarrollox.backend.core.JwtSecurity;

import com.desarrollox.backend.api_register.model.User;
import io.jsonwebtoken.Claims;

public interface ITokenService {
    Claims extractAllClaims(String token);
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    Long getTokenExpiration();
    boolean validateToken(String token);
}