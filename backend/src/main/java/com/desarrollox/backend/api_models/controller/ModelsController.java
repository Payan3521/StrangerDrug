package com.desarrollox.backend.api_models.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/models")
public class ModelsController {
    
    @PostMapping
    public ResponseEntity<?> create(){
        return ResponseEntity.ok("Create");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(){
        return ResponseEntity.ok("Delete");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(){
        return ResponseEntity.ok("Update");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(){
        return ResponseEntity.ok("Find by Id");
    }

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok("Find All");
    }

    @GetMapping("/name")
    public ResponseEntity<?> findByName(){
        return ResponseEntity.ok("Find by Name");
    }

    @GetMapping("/salients-models")
    public ResponseEntity<?> findSalientsModels(){
        return ResponseEntity.ok("Find Salients Models");
    }
}