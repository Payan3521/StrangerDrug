package com.desarrollox.backend.api_purchases.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchasesController {
    
    @PostMapping
    public ResponseEntity<?> create(){
        return ResponseEntity.ok("Create");
    }

    @DeleteMapping("/soft-delete-cliente/{id}")
    public ResponseEntity<?> softDeleteCliente(){
        return ResponseEntity.ok("Soft Delete Cliente");
    }

    @DeleteMapping("/soft-delete-admin/{id}")
    public ResponseEntity<?> softDeleteAdmin(){
        return ResponseEntity.ok("Soft Delete Admin");
    }

    @DeleteMapping("/soft-delete-clear-admin")
    public ResponseEntity<?> softDeleteClearAdmin(){
        return ResponseEntity.ok("Soft Delete Clear Admin");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(){
        return ResponseEntity.ok("Find by Id");
    }

    @GetMapping("/buyerUser/{buyerUserId}")
    public ResponseEntity<?> findByBuyerUser(){
        return ResponseEntity.ok("Find by Buyer User");
    }

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok("Find All");
    }

}