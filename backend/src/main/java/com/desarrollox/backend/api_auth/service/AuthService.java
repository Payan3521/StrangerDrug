package com.desarrollox.backend.api_auth.service;

import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.desarrollox.backend.api_auth.exception.InvalidCredentialsException;
import com.desarrollox.backend.api_auth.exception.InvalidTokenException;
import com.desarrollox.backend.api_auth.model.AuthenticationResult;
import com.desarrollox.backend.api_auth.model.LoginAttempt;
import com.desarrollox.backend.api_auth.model.RefreshToken;
import com.desarrollox.backend.api_auth.repository.ILoginAttemptRepository;
import com.desarrollox.backend.api_auth.repository.IRefreshTokenRepository;
import com.desarrollox.backend.api_register.exception.UserNotFoundException;
import com.desarrollox.backend.api_register.model.User;
import com.desarrollox.backend.api_register.repository.IRegisterRepository;
import com.desarrollox.backend.core.JwtSecurity.ITokenService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    
    private final ILoginAttemptRepository loginAttemptRepository;
    private final IRefreshTokenRepository refreshTokenRepository;
    private final ITokenService tokenService;
    private final IRegisterRepository registerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResult authenticate(String email, String password, String ipAddress, String userAgent) {
        User user = registerRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        if(!passwordEncoder.matches(password, user.getPassword())){
            recordFailedAttempt(email, ipAddress, userAgent, "Contraseña incorrecta");
            throw new InvalidCredentialsException("Contraseña incorrecta");
        }

        //revocar tokens anteriores
        refreshTokenRepository.revokeAllByUserEmail(email);

        //generar tokens
        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        saveRefreshToken(refreshToken, user.getEmail());

        recordSuccessfulAttempt(email, ipAddress, userAgent);

        return AuthenticationResult.success(accessToken, refreshToken, tokenService.getTokenExpiration(), user);
    }

    @Override
    public AuthenticationResult refreshToken(String refreshToken) {
        RefreshToken refresh = refreshTokenRepository.findByToken(refreshToken)
            .orElseThrow(() -> new InvalidTokenException("Refresh token no encontrado"));

        if (!refresh.isValid()) {
            throw new InvalidTokenException("Refresh token no es válido");
        }

        User user = registerRepository.findByEmail(refresh.getUserEmail())
            .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        String newAccessToken = tokenService.generateAccessToken(user);
        String newRefreshToken = tokenService.generateRefreshToken(user);

        refresh.revoke();

        refreshTokenRepository.save(refresh);

        saveRefreshToken(newRefreshToken, user.getEmail());

        return AuthenticationResult.success(newAccessToken, newRefreshToken, tokenService.getTokenExpiration(), user);
    }

    @Override
    public Void logout(String refreshToken) {
        RefreshToken refresh = refreshTokenRepository.findByToken(refreshToken)
            .orElseThrow(() -> new InvalidTokenException("Refresh token no encontrado"));

        refresh.revoke();

        refreshTokenRepository.save(refresh);

        return null;
    }

    @Override
    public Boolean validateToken(String token) {
        boolean isValid = tokenService.validateToken(token);
        return isValid;
    }

    private void saveRefreshToken(String refreshToken, String email) {
        RefreshToken refresh = RefreshToken.builder()
            .token(refreshToken)
            .userEmail(email)
            .expiryDate(LocalDateTime.now().plusDays(30))
            .revoked(false)
            .createdAt(LocalDateTime.now())
            .build();

        refreshTokenRepository.save(refresh);
    }

    private void recordFailedAttempt(String email, String ipAddress, String userAgent, String message) {
        LoginAttempt attempt = LoginAttempt.failed(email, ipAddress, userAgent, message);
        loginAttemptRepository.save(attempt);
    }

    private void recordSuccessfulAttempt(String email, String ipAddress, String userAgent) {
        LoginAttempt attempt = LoginAttempt.successful(email, ipAddress, userAgent);
        loginAttemptRepository.save(attempt);
    }


}
