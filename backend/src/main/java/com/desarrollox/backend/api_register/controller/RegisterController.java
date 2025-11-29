package com.desarrollox.backend.api_register.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegisterController {
    
    @PostMapping
    public ResponseEntity<?> register(){
        return ResponseEntity.ok("Register");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(){
        return ResponseEntity.ok("Find by Id");
    }

    @GetMapping("/email")
    public ResponseEntity<?> findByEmail(){
        return ResponseEntity.ok("Find by Email");
    }
    
}