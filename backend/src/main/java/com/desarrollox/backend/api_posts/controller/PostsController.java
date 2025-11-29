package com.desarrollox.backend.api_posts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostsController {
    
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

    @GetMapping("/model-name")
    public ResponseEntity<?> findByModelName(){
        return ResponseEntity.ok("Find by Model Name");
    }

    @GetMapping("/section-name")
    public ResponseEntity<?> findBySectionName(){
        return ResponseEntity.ok("Find by Section Name");
    }

    @GetMapping("/title")
    public ResponseEntity<?> findByTitle(){
        return ResponseEntity.ok("Find by Title");
    }

    @GetMapping("/recent")
    public ResponseEntity<?> findRecent(){
        return ResponseEntity.ok("Find Recent");
    }
}