package com.desarrollox.backend.api_auth.model;

import com.desarrollox.backend.api_register.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResult {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private User user;
    private String scope;
    private String message;

    public static AuthenticationResult success(String accessToken, String refreshToken, Long expiresIn, User user) {
        return AuthenticationResult.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .expiresIn(expiresIn)
            .user(user)
            .scope("read write")
            .build();
    }

    public static AuthenticationResult failure(String message) {
        return AuthenticationResult.builder()
            .message(message)
            .build();
    }
}