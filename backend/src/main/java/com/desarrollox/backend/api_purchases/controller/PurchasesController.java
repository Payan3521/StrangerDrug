package com.desarrollox.backend.api_purchases.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.desarrollox.backend.api_purchases.controller.dto.PurchaseDto;
import com.desarrollox.backend.api_purchases.model.Purchase;
import com.desarrollox.backend.api_purchases.service.IPurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchasesController {

    private final IPurchaseService purchaseService;
    
    @PostMapping
    public ResponseEntity<Purchase> create(@Valid @RequestBody PurchaseDto purchase){
        Purchase saved = purchaseService.create(purchase);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping("/soft-delete-cliente/{id}")
    public ResponseEntity<Purchase> softDeleteCliente(@PathVariable Long id){
        purchaseService.softDeleteCliente(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/soft-delete-admin/{id}")
    public ResponseEntity<Purchase> softDeleteAdmin(@PathVariable Long id){
        Optional<Purchase> purchase = purchaseService.softDeleteAdmin(id);
        return new ResponseEntity<>(purchase.get(), HttpStatus.OK);
    }

    @DeleteMapping("/soft-delete-clear-admin")
    public ResponseEntity<Void> softDeleteClearAdmin(){
        purchaseService.clear();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> findById(@PathVariable Long id){
        Optional<Purchase> purchase = purchaseService.findById(id);
        return new ResponseEntity<>(purchase.get(), HttpStatus.OK);
    }

    @GetMapping("/buyerUser/{buyerUserId}")
    public ResponseEntity<List<Purchase>> findByBuyerUser(@PathVariable Long buyerUserId){
        List<Purchase> purchaseList = purchaseService.findByBuyerUserId(buyerUserId);
        if(purchaseList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(purchaseList, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<Purchase>> findAll(){
        List<Purchase> purchaseList = purchaseService.findAll();
        if(purchaseList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(purchaseList, HttpStatus.OK);
    }

}