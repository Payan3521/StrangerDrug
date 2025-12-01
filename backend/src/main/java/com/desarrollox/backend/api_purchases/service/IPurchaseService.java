package com.desarrollox.backend.api_purchases.service;

import java.util.List;
import java.util.Optional;
import com.desarrollox.backend.api_purchases.controller.dto.PurchaseDto;
import com.desarrollox.backend.api_purchases.model.Purchase;

public interface IPurchaseService {
    Purchase create(PurchaseDto purchase);
    Optional<Purchase> softDeleteCliente(Long id);
    Optional<Purchase> findById(Long id);
    List<Purchase> findByBuyerUserId(Long buyerId);
    List<Purchase> findAll();
    Optional<Purchase> softDeleteAdmin(Long id);
    Void clear();
}
