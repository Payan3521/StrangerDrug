package com.desarrollox.backend.api_auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.desarrollox.backend.api_auth.controller.dto.LoginRequest;
import com.desarrollox.backend.api_auth.controller.dto.LoginResponse;
import com.desarrollox.backend.api_auth.controller.dto.LogoutRequest;
import com.desarrollox.backend.api_auth.controller.dto.RefreshTokenRequest;
import com.desarrollox.backend.api_auth.controller.webMapper.AuthenticationWebMapper;
import com.desarrollox.backend.api_auth.model.AuthenticationResult;
import com.desarrollox.backend.api_auth.service.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final IAuthService authService;
    private final AuthenticationWebMapper authenticationWebMapper;
    
    //endpoint para iniciar sesión
    //valida credenciales como usuario y contraseña
    //retorna un token de acceso y un token de refresco
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
        @Valid
        @RequestBody LoginRequest loginRequest,
        HttpServletRequest request
    ){
        String ipAddress = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");

        AuthenticationResult result = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword(), ipAddress, userAgent);
        LoginResponse response = authenticationWebMapper.toLoginResponse(result);

        return ResponseEntity.ok(response);
    }

    //endpoint para refrescar el token de acceso
    //recibe un token de refresco
    //retorna un token de acceso y un token de refresco
    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        AuthenticationResult result = authService.refreshToken(refreshTokenRequest.getRefreshToken());
        LoginResponse response = authenticationWebMapper.toLoginResponse(result);

        return ResponseEntity.ok(response);
    }

    //endpoint para cerrar sesión
    //recibe un token de acceso
    //retorna un mensaje de éxito
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest logoutRequest){
        authService.logout(logoutRequest.getRefreshToken());
        return ResponseEntity.ok().build();
    }

    //endpoint para verificar el token de acceso
    //recibe un token de acceso
    //retorna un mensaje de éxito
    @PostMapping("/check-token")
    public ResponseEntity<Boolean> checkToken(@RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.substring("Bearer ".length());
        boolean isValid = authService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}