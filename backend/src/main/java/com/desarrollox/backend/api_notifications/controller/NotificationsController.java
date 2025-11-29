package com.desarrollox.backend.api_notifications.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationsController {
    
    @PostMapping
    public ResponseEntity<?> send(){
        return ResponseEntity.ok("send");
    }

    @GetMapping("/{receiveId}")
    public ResponseEntity<?> receive(){
        return ResponseEntity.ok("receive");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(){
        return ResponseEntity.ok("delete");
    }

    @DeleteMapping("/clear/{receiveId}")
    public ResponseEntity<?> clear(){
        return ResponseEntity.ok("clear");
    }
}
