package com.desarrollox.backend.api_auth.controller.webMapper;

import org.springframework.stereotype.Component;
import com.desarrollox.backend.api_auth.controller.dto.LoginResponse;
import com.desarrollox.backend.api_auth.model.AuthenticationResult;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationWebMapper {
    public LoginResponse toLoginResponse(AuthenticationResult result){
        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.builder()
            .id(result.getUser().getId())
            .name(result.getUser().getName())
            .email(result.getUser().getEmail())
            .role(result.getUser().getRole().name())
            .birthdate(result.getUser().getBirthdate())
            .build();

        LoginResponse response = LoginResponse.builder()
            .accessToken(result.getAccessToken())
            .refreshToken(result.getRefreshToken())
            .tokenType(result.getTokenType())
            .expiresIn(result.getExpiresIn())
            .user(userInfo)
            .scope(result.getScope())
            .build();

        return response;
    }
}