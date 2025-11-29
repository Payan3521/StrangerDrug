package com.desarrollox.backend.api_videos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideosController {

    @PostMapping("/upload-video")
    public ResponseEntity<?> uploadVideo(){
        return ResponseEntity.ok("Upload Video");
    }

    @PostMapping("/upload-preview")
    public ResponseEntity<?> uploadPreview(){
        return ResponseEntity.ok("Upload Preview");
    }

    @PostMapping("/upload-thumbnail")
    public ResponseEntity<?> uploadThumbnail(){
        return ResponseEntity.ok("Upload Thumbnail");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(){
        return ResponseEntity.ok("Find by Id");
    }

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok("Find All");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(){
        return ResponseEntity.ok("Update");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(){
        return ResponseEntity.ok("Delete");
    }
}