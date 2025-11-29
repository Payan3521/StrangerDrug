package com.desarrollox.backend.core.JwtSecurity;

import io.jsonwebtoken.Claims;

public interface ITokenService {
    Claims extractAllClaims(String token);
}