package com.desarrollox.backend.api_auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    
    //endpoint para iniciar sesión
    //valida credenciales como usuario y contraseña
    //retorna un token de acceso y un token de refresco
    @PostMapping("/login")
    public ResponseEntity<?> login(){
        return ResponseEntity.ok("Login");
    }

    //endpoint para refrescar el token de acceso
    //recibe un token de refresco
    //retorna un token de acceso y un token de refresco
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(){
        return ResponseEntity.ok("Refresh Token");
    }

    //endpoint para cerrar sesión
    //recibe un token de acceso
    //retorna un mensaje de éxito
    @PostMapping("/logout")
    public ResponseEntity<?> logout(){
        return ResponseEntity.ok("Logout");
    }

    //endpoint para verificar el token de acceso
    //recibe un token de acceso
    //retorna un mensaje de éxito
    @PostMapping("/check-token")
    public ResponseEntity<?> checkToken(){
        return ResponseEntity.ok("Check Token");
    }
}