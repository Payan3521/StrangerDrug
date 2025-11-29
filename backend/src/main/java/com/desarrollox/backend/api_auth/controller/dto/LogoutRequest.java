package com.desarrollox.backend.api_auth.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LogoutRequest {
    private String refreshToken;
}
