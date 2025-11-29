package com.desarrollox.backend.api_auth.service;

import com.desarrollox.backend.api_auth.model.AuthenticationResult;

public interface IAuthService {
    AuthenticationResult authenticate(String email, String password, String ipAddress, String userAgent);
    AuthenticationResult refreshToken(String refreshToken);
    Void logout(String refreshToken);
    Boolean validateToken(String token);
}
