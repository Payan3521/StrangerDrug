package com.desarrollox.backend.api_register.controller;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.desarrollox.backend.api_register.model.User;
import com.desarrollox.backend.api_register.service.IRegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegisterController {
    private final IRegisterService registerService;
    
    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user){
        User saved = registerService.create(user);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){
        Optional<User> user = registerService.findById(id);
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @GetMapping("/email")
    public ResponseEntity<User> findByEmail(@RequestParam String email){
        Optional<User> user = registerService.findByEmail(email);
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }
    
}