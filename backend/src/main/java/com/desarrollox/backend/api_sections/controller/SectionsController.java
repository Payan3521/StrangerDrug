package com.desarrollox.backend.api_sections.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sections")
@RequiredArgsConstructor
public class SectionsController {
    
    @PostMapping
    public ResponseEntity<?> create(){
        return ResponseEntity.ok("Create");
    }

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok("Find All");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(){
        return ResponseEntity.ok("Find by Id");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(){
        return ResponseEntity.ok("Delete");
    }
}
