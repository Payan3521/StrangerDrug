package com.desarrollox.backend.api_payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    @GetMapping("/initiate")   
    public ResponseEntity<?> initiate(){
        return ResponseEntity.ok("Initiate");
    }

    @GetMapping("/postback")
    public ResponseEntity<?> postback(){
        return ResponseEntity.ok("Postback");
    }

}
